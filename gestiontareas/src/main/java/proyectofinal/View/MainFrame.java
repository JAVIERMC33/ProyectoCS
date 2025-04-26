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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {
    private final TareaService tareaService;
    private JTable tareasTable;
    private DefaultTableModel tableModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JTextField searchField;
    private ScheduledExecutorService searchScheduler;
    
    public MainFrame(TareaService tareaService) {
        this.tareaService = tareaService;
        this.searchScheduler = Executors.newSingleThreadScheduledExecutor();
        initializeUI();
        loadTasks();
    }
    
    private void initializeUI() {
        setTitle("Gestión de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        // Modelo de tabla
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
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadTasks();
        });
        
        filterPanel.add(filterPriorityButton);
        filterPanel.add(filterStatusButton);
        filterPanel.add(showAllButton);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(e -> showAddDialog());
        
        JButton editButton = new JButton("Editar");
        editButton.addActionListener(e -> showEditDialog());
        
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(e -> deleteTask());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Panel superior combinado
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        
        // Agregar componentes al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(tareasTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void scheduleSearch() {
        searchScheduler.schedule(() -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                loadTasks();
            } else {
                List<Tarea> resultados = tareaService.buscarPorPalabraClave(searchText);
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    mostrarTareasEnTabla(resultados);
                });
            }
        }, 300, TimeUnit.MILLISECONDS); // Retardo de 300ms para evitar búsquedas con cada tecla
    }
    
    private void loadTasks() {
        tableModel.setRowCount(0);
        List<Tarea> tareas = tareaService.obtenerTodasLasTareas();
        mostrarTareasEnTabla(tareas);
    }
    
    private void mostrarTareasEnTabla(List<Tarea> tareas) {
        for (Tarea tarea : tareas) {
            Object[] row = {
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFechaVencimiento().format(dateFormatter),
                tarea.getPrioridad().toString(),
                tarea.getEstado().toString()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Agregar Tarea", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);
        JTextField dateField = new JTextField(LocalDate.now().format(dateFormatter));
        JComboBox<Prioridad> priorityCombo = new JComboBox<>(Prioridad.values());
        JComboBox<Estado> statusCombo = new JComboBox<>(Estado.values());
        JButton saveButton = new JButton("Guardar");
        
        panel.add(new JLabel("Título*:"));
        panel.add(titleField);
        panel.add(new JLabel("Descripción*:"));
        panel.add(descScroll);
        panel.add(new JLabel("Fecha (dd/MM/yyyy)*:"));
        panel.add(dateField);
        panel.add(new JLabel("Prioridad*:"));
        panel.add(priorityCombo);
        panel.add(new JLabel("Estado*:"));
        panel.add(statusCombo);
        panel.add(new JLabel("* Campos obligatorios"));
        panel.add(saveButton);
        
        saveButton.addActionListener(e -> {
            try {
                // Validación de campos obligatorios
                if (titleField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("El título no puede estar vacío");
                }
                if (descArea.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("La descripción no puede estar vacía");
                }
                
                LocalDate fecha = LocalDate.parse(dateField.getText(), dateFormatter);
                
                // Validación de fecha (permite el día actual)
                if (fecha.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha no puede ser anterior al día actual");
                }
                
                // Crear la tarea
                Tarea nuevaTarea = new Tarea(
                    null,
                    titleField.getText(),
                    descArea.getText(),
                    fecha,
                    (Prioridad) priorityCombo.getSelectedItem(),
                    (Estado) statusCombo.getSelectedItem()
                );
                
                tareaService.crearTarea(nuevaTarea);
                loadTasks();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Tarea creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    @Override
    public void dispose() {
        searchScheduler.shutdownNow();
        super.dispose();
    }
    
    private void showEditDialog() {
        int selectedRow = tareasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long taskId = (Long) tableModel.getValueAt(selectedRow, 0);
        var tareaOpt = tareaService.obtenerTareaPorId(taskId);
        
        if (!tareaOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "Tarea no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Tarea tarea = tareaOpt.get();
        JDialog dialog = new JDialog(this, "Editar Tarea", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField titleField = new JTextField(tarea.getTitulo());
        JTextArea descArea = new JTextArea(tarea.getDescripcion(), 3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);
        JTextField dateField = new JTextField(tarea.getFechaVencimiento().format(dateFormatter));
        JComboBox<Prioridad> priorityCombo = new JComboBox<>(Prioridad.values());
        priorityCombo.setSelectedItem(tarea.getPrioridad());
        JComboBox<Estado> statusCombo = new JComboBox<>(Estado.values());
        statusCombo.setSelectedItem(tarea.getEstado());
        JButton saveButton = new JButton("Guardar");
        
        panel.add(new JLabel("Título*:"));
        panel.add(titleField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descScroll);
        panel.add(new JLabel("Fecha (dd/MM/yyyy)*:"));
        panel.add(dateField);
        panel.add(new JLabel("Prioridad*:"));
        panel.add(priorityCombo);
        panel.add(new JLabel("Estado*:"));
        panel.add(statusCombo);
        panel.add(new JLabel("* Campos obligatorios"));
        panel.add(saveButton);
        
        saveButton.addActionListener(e -> {
            try {
                LocalDate fecha = LocalDate.parse(dateField.getText(), dateFormatter);
                
                // Validación adicional de fecha
                if (fecha.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha no puede ser anterior al día actual");
                }
                
                tarea.setTitulo(titleField.getText());
                tarea.setDescripcion(descArea.getText());
                tarea.setFechaVencimiento(fecha);
                tarea.setPrioridad((Prioridad) priorityCombo.getSelectedItem());
                tarea.setEstado((Estado) statusCombo.getSelectedItem());
                
                if (tareaService.actualizarTarea(tarea)) {
                    loadTasks();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Tarea actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "No se pudo actualizar la tarea", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Formato de fecha inválido. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteTask() {
        int selectedRow = tareasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarea para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar esta tarea?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            Long taskId = (Long) tableModel.getValueAt(selectedRow, 0);
            if (tareaService.eliminarTarea(taskId)) {
                loadTasks();
                JOptionPane.showMessageDialog(this, "Tarea eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarea", "Error", JOptionPane.ERROR_MESSAGE);
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
            List<Tarea> tareas = tareaService.filtrarPorPrioridad(selected);
            tableModel.setRowCount(0);
            mostrarTareasEnTabla(tareas);
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
            List<Tarea> tareas = tareaService.filtrarPorEstado(selected);
            tableModel.setRowCount(0);
            mostrarTareasEnTabla(tareas);
        }
    }

    private DocumentListener createSearchDocumentListener() {
        return new DocumentListener() {
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
        };
    }
}