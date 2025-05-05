package proyectofinal.View;

import proyectofinal.Model.Estado;
import proyectofinal.Model.Prioridad;
import proyectofinal.Model.Tarea;
import proyectofinal.Servicio.TareaService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainFrame extends JFrame {
    private static final String APP_TITLE = "Gestión de Tareas";
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private final TareaService tareaService;
    private final ScheduledExecutorService searchScheduler;
    private DefaultTableModel tableModel;
    private JTable tareasTable;
    private JTextField searchField;

    public MainFrame(TareaService tareaService) {
        super(APP_TITLE);
        this.tareaService = tareaService;
        this.searchScheduler = Executors.newSingleThreadScheduledExecutor();
        initializeUI();
        loadTasks();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        setupTable();
        setupMainPanel();
    }

    private void setupTable() {
        String[] columnNames = {"ID", "Título", "Descripción", "Fecha Vencimiento", "Prioridad", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Long.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tareasTable = new JTable(tableModel);
        tareasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tareasTable.setAutoCreateRowSorter(true);
    }

    private void setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(createSearchFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tareasTable), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createSearchFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        searchField.setToolTipText("Buscar por título o descripción...");
        searchField.getDocument().addDocumentListener(createSearchDocumentListener());
        
        searchPanel.add(new JLabel("Buscar:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton filterPriorityButton = new JButton("Filtrar por Prioridad");
        filterPriorityButton.addActionListener(e -> filterByPriority());
        
        JButton filterStatusButton = new JButton("Filtrar por Estado");
        filterStatusButton.addActionListener(e -> filterByStatus());
        
        JButton showAllButton = new JButton("Mostrar Todas");
        showAllButton.addActionListener(e -> resetView());
        
        filterPanel.add(filterPriorityButton);
        filterPanel.add(filterStatusButton);
        filterPanel.add(showAllButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(e -> showAddDialog());
        
        JButton editButton = new JButton("Editar");
        editButton.addActionListener(e -> showEditDialog());
        
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> deleteTask());
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        
        return panel;
    }

    private void resetView() {
        searchField.setText("");
        loadTasks();
    }

    private void loadTasks() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Tarea> tareas = tareaService.obtenerTodasLasTareas();
            mostrarTareasEnTabla(tareas);
        });
    }

    private void mostrarTareasEnTabla(List<Tarea> tareas) {
        for (Tarea tarea : tareas) {
            Object[] row = {
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFechaVencimiento().format(DATE_FORMATTER),
                tarea.getPrioridad().toString(),
                tarea.getEstado().toString()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        new TaskDialog(this, "Agregar Tarea", tarea -> {
            tareaService.crearTarea(tarea);
            loadTasks();
        }).setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = tareasTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Seleccione una tarea para editar");
            return;
        }
        
        Long taskId = (Long) tableModel.getValueAt(selectedRow, 0);
        var tareaOpt = tareaService.obtenerTareaPorId(taskId);
        
        if (tareaOpt.isEmpty()) {
            showError("Tarea no encontrada");
            return;
        }
        
        Tarea tareaOriginal = tareaOpt.get();
        new TaskDialog(this, "Editar Tarea", tareaOriginal, tareaEditada -> {
            try {
                // Mantener el ID original de la tarea
                tareaEditada.setId(tareaOriginal.getId());
                
                if (tareaService.actualizarTarea(tareaEditada)) {
                    loadTasks();
                    showInfo("Tarea actualizada exitosamente");
                } else {
                    showError("No se pudo actualizar la tarea");
                }
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        }).setVisible(true);
    }

    private void deleteTask() {
        int selectedRow = tareasTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Seleccione una tarea para eliminar");
            return;
        }
        
        if (confirmAction("¿Está seguro de eliminar esta tarea?")) {
            Long taskId = (Long) tableModel.getValueAt(selectedRow, 0);
            if (tareaService.eliminarTarea(taskId)) {
                loadTasks();
                showInfo("Tarea eliminada exitosamente");
            } else {
                showError("No se pudo eliminar la tarea");
            }
        }
    }

    private void filterByPriority() {
        Prioridad selected = (Prioridad) JOptionPane.showInputDialog(
            this,
            "Seleccione prioridad:",
            "Filtrar por Prioridad",
            JOptionPane.PLAIN_MESSAGE,
            null,
            Prioridad.values(),
            Prioridad.MEDIA
        );
        
        if (selected != null) {
            filterTasks(() -> tareaService.filtrarPorPrioridad(selected));
        }
    }

    private void filterByStatus() {
        Estado selected = (Estado) JOptionPane.showInputDialog(
            this,
            "Seleccione estado:",
            "Filtrar por Estado",
            JOptionPane.PLAIN_MESSAGE,
            null,
            Estado.values(),
            Estado.PENDIENTE
        );
        
        if (selected != null) {
            filterTasks(() -> tareaService.filtrarPorEstado(selected));
        }
    }

    private void filterTasks(Supplier<List<Tarea>> filterSupplier) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            mostrarTareasEnTabla(filterSupplier.get());
        });
    }

    private DocumentListener createSearchDocumentListener() {
        return new DocumentListener() {
            private ScheduledFuture<?> lastTask;
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleSearch();
            }
    
            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleSearch();
            }
    
            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleSearch();
            }
            
            private void scheduleSearch() {
                if (lastTask != null && !lastTask.isDone()) {
                    lastTask.cancel(false);
                }
                
                lastTask = searchScheduler.schedule(() -> {
                    String searchText = searchField.getText().trim();
                    List<Tarea> resultados = searchText.isEmpty() ? 
                        tareaService.obtenerTodasLasTareas() : 
                        tareaService.buscarPorPalabraClave(searchText);
                        
                    SwingUtilities.invokeLater(() -> {
                        tableModel.setRowCount(0);
                        mostrarTareasEnTabla(resultados);
                    });
                }, 300, TimeUnit.MILLISECONDS);
            }
        };
    }

    // Helpers para mensajes
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean confirmAction(String message) {
        return JOptionPane.showConfirmDialog(
            this, 
            message, 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    @Override
    public void dispose() {
        try {
            searchScheduler.shutdown();
            if (!searchScheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                searchScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            searchScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        super.dispose();
    }
}

// Clase para el diálogo de tareas (nueva clase separada)
class TaskDialog extends JDialog {
    private final JTextField titleField = new JTextField();
    private final JTextArea descArea = new JTextArea(3, 20);
    private final JTextField dateField = new JTextField(LocalDate.now().format(MainFrame.DATE_FORMATTER));
    private final JComboBox<Prioridad> priorityCombo = new JComboBox<>(Prioridad.values());
    private final JComboBox<Estado> statusCombo = new JComboBox<>(Estado.values());

    public TaskDialog(JFrame parent, String title, Consumer<Tarea> onSave) {
        this(parent, title, null, onSave);
    }

    public TaskDialog(JFrame parent, String title, Tarea tarea, Consumer<Tarea> onSave) {
        super(parent, title, true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        if (tarea != null) {
            titleField.setText(tarea.getTitulo());
            descArea.setText(tarea.getDescripcion());
            dateField.setText(tarea.getFechaVencimiento().format(MainFrame.DATE_FORMATTER));
            priorityCombo.setSelectedItem(tarea.getPrioridad());
            statusCombo.setSelectedItem(tarea.getEstado());
        }
        
        setupUI(onSave);
    }

    private void setupUI(Consumer<Tarea> onSave) {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Título*:"));
        panel.add(titleField);
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(descArea));
        panel.add(new JLabel("Fecha (dd/MM/yyyy)*:"));
        panel.add(dateField);
        panel.add(new JLabel("Prioridad*:"));
        panel.add(priorityCombo);
        panel.add(new JLabel("Estado*:"));
        panel.add(statusCombo);
        panel.add(new JLabel("* Campos obligatorios"));
        
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> saveTask(onSave));
        panel.add(saveButton);
        
        add(panel);
    }

    private void saveTask(Consumer<Tarea> onSave) {
        try {
            validateFields();
            LocalDate fecha = parseDate();
            
            Tarea tarea = new Tarea(
                null, // ID se asignará al guardar
                titleField.getText().trim(),
                descArea.getText().trim(),
                fecha,
                (Prioridad) priorityCombo.getSelectedItem(),
                (Estado) statusCombo.getSelectedItem()
            );
            
            onSave.accept(tarea);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validateFields() {
        if (titleField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (dateField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
    }

    private LocalDate parseDate() {
        try {
            LocalDate fecha = LocalDate.parse(dateField.getText(), MainFrame.DATE_FORMATTER);
            if (fecha.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha no puede ser anterior al día actual");
            }
            return fecha;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
        }
    }
}