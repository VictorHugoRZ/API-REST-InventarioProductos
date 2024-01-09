package vr.inventarios.controlador;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vr.inventarios.excepcion.RecursoNoEncontrado;
import vr.inventarios.modelo.Producto;
import vr.inventarios.servicio.ProductoServicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Este controlador es de tipo REST, por lo tanto tenemos que agregar configuraciones extra a nuestro codigo
//para que Spring nos permita definir los Endpoints con los que haremos peticiones a traves de nuestra aplicacion
//hecha con Angular
@RestController //Le indicamos a Spring que este es un controlador de tipo Rest

//Definiremos el Context Pad, que es el origen y nombre de la aplicacion en los Endpoints, en este caso
//Context Pad = inventario-app/v1
//Si no definimos esto entonces las peticiones solo se realizarian a http://localhost:8080/productos por ejemplo.
//Pero definiendo el Context Pad las peticiones serian asi: http://localhost:8080/inventario-app/v1/productos
@RequestMapping("inventario-app/v1")

//Ya que se realizaran peticiones desde una aplicacion hecha con Angular podemos definir el permiso para esta con su IP.
@CrossOrigin("http://localhost:4200") //Es el puerto donde se levantan las aplicaciones de Angular por defecto.
public class ProductoControlador {

    //Definimos nuestro Logger para mostrar informacion a traves de la consola
    //Tenemos que especificar a nuestro Logger desde donde estara recibiendo informacion, en este caso sera
    //a traves de la clase ProductoControlador, al final solo agregamos el .class para especificar que
    //esta es una clase.
    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    //Inyectamos las dependencias de la capa de Servicio
    //Con esta inyeccion de dependencias ya tenemos todos los metodos necesarios para obtener o mandar informacion
    //a la base de datos
    @Autowired
    private ProductoServicio productoServicio;

    //Creamos el metodo para la obtencion de los Productos READ/GET
    //Nuestro Endpoint para la obtencion de Productos seria: http://localhost:8080/inventario-app/v1/productos
    @GetMapping("/productos")
    public List<Producto> getProducts() { //Metodo que retorna una Lista de Productos
        List<Producto> productos = this.productoServicio.listarProductos(); //Uso de dependencias para obtener datos
        logger.info("Productos obtenidos: ");
        productos.forEach(producto -> logger.info(producto.toString())); //Mostramos cada producto
        return productos;
    }

    //Creamos el metodo para crear productos CREATE/POST
    //Nuestro Endpoint para la creacion de Productos seria: http://localhost:8080/inventario-app/v1/productos
    //La diferencia es que usaremos una etiqueta diferente a @GetMapping
    //Tenemos que especificarle al metodo que recibira un objeto de tipo Producto como argumento
    //Esto lo hacemos con la etiqueta @RequestBody
    @PostMapping("/productos")
    public Producto postProducts(@RequestBody Producto producto) { //Metodo que retorna un objeto de tipo Producto
        logger.info("Producto a agregar: " + producto); //Mostramos en consola el Producto que se agregara
        return this.productoServicio.guardarProducto(producto); //Retornamos informacion del Producto
    }

    //Creamos el metodo para la obtencion de productos por Id CREATE/GET
    //Nuestro Endpoint para buscar productos por Id seria: http://localhost:8080/inventario-app/v1/productos/{id}
    //Usamos la etiqueta GetMapping para filtrar informacion en la base de datos

    //Nuestro metodo retornara un objeto de tipo Producto envuelto en un objeto de tipo Response Entity
    //Se retorna el objeto de tipo Producto dentro de cuerpo de la respuesta de tipo GET

    //Tenemos que usar y especificar que nuestro metodo recibira un valor mediante el Path de la consulta,
    //esto lo hacemos con la etiqueta @PathVariable para inmediatamente especificar el tipo de dato y nombre
    //del parametro que sera enviado
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable int id) {
        //Se crea un objeto de tipo Producto, su valor sera igual a un objeto de tipo Optional,
        //este sera el Producto que coincida con el Id que se proporciono con la consulta
        Producto producto = this.productoServicio.buscarProductoPorId(id);

        if(producto != null) {
            //Retornamos el objeto de tipo ResponseEntity, este lanzara un estatus 200(Ok) al usar el metodo ok();
            //Ademas llevara el objeto de tipo Producto hacia el Cliente para mostrarlo
            return ResponseEntity.ok(producto);
        } else {
            throw new RecursoNoEncontrado("No se encontro el Id: " + id);
        }
    }

    //Creamos el metodo para actualizar Productos por Id Update/PUT
    //Nuestro Endpoint para actualizar productos por Id seria: http://localhost:8080/inventario-app/v1/productos/{id}
    //Usamos la etiqueta @PutMapping para actualizar informacion en la base de datos

    //Nuestro metodo retornara un objeto de tipo Producto envuelto en un objeto de tipo Response Entity
    //Se retorna el objeto de tipo Producto dentro de cuerpo de la respuesta de tipo PUT

    //Tenemos que usar y especificar que nuestro metodo recibira un valor mediante el Path de la consulta,
    //esto lo hacemos con la etiqueta @PathVariable para inmediatamente especificar el tipo de dato y nombre
    //del parametro que sera enviado

    //Tenemos que especificarle al metodo que recibira un objeto de tipo Producto como argumento
    //Esto lo hacemos con la etiqueta @RequestBody
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable int id, @RequestBody Producto productoRecibido) {
        //Verificamos que el Producto exista en la base de datos, primero lo buscaremos
        Producto producto = this.productoServicio.buscarProductoPorId(id);

        if (producto != null) {
            //Seteamos los valores con el objeto que nos retorna el metodo buscarProductoPorId();
            producto.setDescripcion(productoRecibido.getDescripcion());
            producto.setPrecio(productoRecibido.getPrecio());
            producto.setExistencia(productoRecibido.getExistencia());
            this.productoServicio.guardarProducto(producto);
            return ResponseEntity.ok(producto);
        } else {
            //Manejamos la excepcion en caso de que se quiera actualizar un producto que no existe
            //Al no manejar esta excepcion se recibe un error de tipo 500, este error nos indica un fallo
            //interno en el servidor, mas sin en cambio, al manejar la excepcion de esta manera,
            //el codigo de status sera 404, para indicarle al cliente que el Producto que desea actualizar
            //no existe
            throw new RecursoNoEncontrado("No existe el producto con el id: " + id);
        }
    }

    //Creamos el metodo para Eliminar Productos por Id Delete/DELETE
    //Nuestro Endpoint para eliminar productos por Id seria: http://localhost:8080/inventario-app/v1/productos/{id}
    //Usamos la etiqueta @DeleteMapping para eliminar informacion en la base de datos

    //Nuestro metodo retornara un String y un Boolean para que el cliente pueda manejar los datos
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@PathVariable int id) {
        //Verificamos que el Producto exista en la base de datos, primero lo buscaremos
        Producto producto = this.productoServicio.buscarProductoPorId(id);

        if (producto != null) {
            //Eliminamos el producto obteniendo el Id del Producto que se busco anteriormente en la base de datos
            this.productoServicio.eliminarProductoPorId(producto.getIdProducto());

            //Declaramos la variable que contendra los datos que se daran como respuesta
            Map<String, Boolean> respuesta = new HashMap<>();
            //Seteamos los valores que se daran como respuesta
            respuesta.put("Producto eliminado: ", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);
        } else {
            //Manejamos la excepcion en caso de que se quiera eliminar un producto que no existe
            //Al no manejar esta excepcion se recibe un error de tipo 500, este error nos indica un fallo
            //interno en el servidor, mas sin en cambio, al manejar la excepcion de esta manera,
            //el codigo de status sera 404, para indicarle al cliente que el Producto que desea eliminar
            //no existe
            throw new RecursoNoEncontrado("No se puede eliminar al producto con el id: " + id + ", ya que no existe");
        }
    }
}
