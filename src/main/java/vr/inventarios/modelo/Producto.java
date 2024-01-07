package vr.inventarios.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//Con la etiqueta de Entidad, si aun no existe esta en la base de datos, se creara de manera automatica
@Entity //Viene de JPA/Hibernate
//Agregamos las etiquetas de Lombok para generar de manera automatica los metodos faltantes
@Data //Getters and Setters
@AllArgsConstructor //All arguments Constructor
@NoArgsConstructor //No arguments Constructor
@ToString //ToString Method
public class Producto {
    //Definimos nuestra llave primaria autoincrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idProducto;
    String descripcion;
    Double precio;
    Integer existencia;
}
