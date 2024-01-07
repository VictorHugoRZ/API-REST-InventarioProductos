package vr.inventarios.servicio;

import vr.inventarios.modelo.Producto;

import java.util.List;

//Interface con firmas de metodos que seran sobreescritos en la capa de servicio
public interface IProductoServicio {
    public List<Producto> listarProductos();

    public Producto buscarProductoPorId(Integer idProducto);

    public void guardarProducto(Producto producto);

    public void eliminarProductoPorId(Integer idProducto);
}
