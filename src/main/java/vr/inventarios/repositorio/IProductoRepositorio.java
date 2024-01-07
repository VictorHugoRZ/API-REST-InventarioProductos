package vr.inventarios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import vr.inventarios.modelo.Producto;

//Componente de Repositorio que exitende sus metodos de el repositorio de JPA
public interface IProductoRepositorio extends JpaRepository<Producto, Integer> {
}
