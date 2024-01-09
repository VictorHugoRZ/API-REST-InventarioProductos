package vr.inventarios.excepcion;

import org.hibernate.collection.spi.PersistentIdentifierBag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


//Esta clase extiende sus propiedades de RunTimeException, esta es una excepcion no obligatoria que se lanzara
//a la clase padre en caso que ser necesario
//Con @ResponseStatus definimos el estatus que se lanzara cuando la excepcion se cree, en este caso esta hecha
//para notificar a la clase padre que el recurso a buscar no fue encontrado (NOT_FOUND)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecursoNoEncontrado extends RuntimeException{

    //Definimos el metodo que mandara el mensaje de la excepcion a la clase padre
    public RecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}
