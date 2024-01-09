package vr.inventarios.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vr.inventarios.modelo.Producto;
import vr.inventarios.repositorio.IProductoRepositorio;

import java.util.List;

//Definimos este componente con la etiqueta de Servicio para que Spring identifique su rol dentro del programa
@Service
public class ProductoServicio implements IProductoServicio{

    //Inyectamos las dependencias de el componente de Repositorio que tiene los metodos apropiados
    //para trabajar con la base de datos
    @Autowired
    private IProductoRepositorio productoRepositorio;

    @Override
    public List<Producto> listarProductos() { //Read
        //Retornamos todos los objetos de tipo Producto de la base de datos, esto lo especificamos con el
        //tipo de dato que retorna la funcion List<Producto>
        return this.productoRepositorio.findAll();
    }

    @Override
    public Producto buscarProductoPorId(Integer idProducto) { //Read
        //Tenemos que especificar con el metodo orElse(); que el valor puede ser null ya que el metodo
        //findById(); nos retorna un objeto de manera opcional, tenemos que manejar esa "excepcion" con
        //el metodo orElse();
        return this.productoRepositorio.findById(idProducto).orElse(null);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        //Guardamos (Create) o Actualizamos registros (Update)
        return this.productoRepositorio.save(producto);
    }

    @Override
    public void eliminarProductoPorId(Integer idProducto) {
        //Borramos registros (Delete)
        this.productoRepositorio.deleteById(idProducto);
    }
}
