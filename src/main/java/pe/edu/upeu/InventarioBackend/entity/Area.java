package pe.edu.upeu.InventarioBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "areas", uniqueConstraints = {
        @UniqueConstraint(name = "uk_area_codigo", columnNames = "codigo"),
        @UniqueConstraint(name = "uk_area_nombre", columnNames = "nombre")
})
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String responsable;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "presupuesto_mensual", nullable = false, precision = 12, scale = 2)
    private BigDecimal presupuestoMensual;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    void prePersist() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = true;
    }

    @PreUpdate
    void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
