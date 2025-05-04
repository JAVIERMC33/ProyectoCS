package proyectofinal.Servicio;

import org.junit.jupiter.api.*;
import proyectofinal.Model.*;
import proyectofinal.Repositorio.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

class TareaServicioImplTest {
    private TareaServicioImpl servicio;
    private TareaRepositorio repositorioMock;
    
    @BeforeEach
    void setUp() {
        repositorioMock = mock(TareaRepositorio.class);
        servicio = new TareaServicioImpl(repositorioMock);
    }
    
    @Test
    void crearTareaValidaLlamaAlRepositorio() {
        Tarea tareaValida = new Tarea(null, "Válida", "Desc", 
                                     LocalDate.now().plusDays(1), 
                                     Prioridad.MEDIA, Estado.PENDIENTE);
        
        when(repositorioMock.guardar(any(Tarea.class))).thenReturn(tareaValida);
        
        Tarea resultado = servicio.crearTarea(tareaValida);
        
        assertNotNull(resultado);
        verify(repositorioMock).guardar(tareaValida);
    }
    
    @Test
void constructorTarea_LanzaExcepcionCuandoFechaEsPasada() {
    assertThrows(IllegalArgumentException.class, () -> {
        new Tarea(null, "Inválida", "Desc", 
                 LocalDate.now().minusDays(1),  // Fecha pasada
                 Prioridad.MEDIA, Estado.PENDIENTE);
    });
}
    
    @Test
    void actualizarTareaNoExistenteLanzaExcepcion() {
        Long idInexistente = 999L;
        Tarea tarea = new Tarea(idInexistente, "Título", "Desc", 
                               LocalDate.now(), Prioridad.MEDIA, Estado.PENDIENTE);
        
        when(repositorioMock.existeConId(idInexistente)).thenReturn(false);
        
        assertThrows(IllegalArgumentException.class, () -> {
            servicio.actualizarTarea(tarea);
        });
    }
    
    @Test
    void filtrarPorEstadoDevuelveListaFiltrada() {
        Estado estado = Estado.COMPLETADA;
        when(repositorioMock.obtenerTodas()).thenReturn(List.of(
            new Tarea(1L, "T1", "D1", LocalDate.now(), Prioridad.ALTA, Estado.COMPLETADA),
            new Tarea(2L, "T2", "D2", LocalDate.now(), Prioridad.MEDIA, Estado.PENDIENTE)
        ));
        
        List<Tarea> resultados = servicio.filtrarPorEstado(estado);
        
        assertEquals(1, resultados.size());
        assertEquals(Estado.COMPLETADA, resultados.get(0).getEstado());
    }
}