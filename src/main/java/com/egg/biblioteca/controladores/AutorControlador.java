package com.egg.biblioteca.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import java.util.logging.Level;
// import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.servicios.AutorServicio;


@RestController
@RequestMapping("/autor") // localhost:8080/autor
public class AutorControlador {

    @Autowired 
    private AutorServicio autorServicio;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAutor(@RequestBody String nombre) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(autorServicio.crearAutor(nombre));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/crearEntidad")
    public ResponseEntity<?> crearAutor(@RequestBody Autor autor) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(autorServicio.crearAutorEntidad(autor));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/registrar") // localhost:8080/autor/registrar
    public String registrar() {
        return "autor_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo){
        try {
            autorServicio.crearAutor(nombre);    // llamo a mi servicio para persistir
            modelo.put("exito", "El autor fue cargado correctamente.");       
        } catch (MiException ex) {    
            modelo.put("error", ex.getMessage());      
            // Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autor_form.html";
        }        
        return "inicio.html";
    
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap model) {
        model.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }

    @PostMapping("{id}")
    public String modificar(@PathVariable UUID id, String nombre, ModelMap model) {
        try{
            autorServicio.modificarAutor(nombre, id);
            return "redirect:../lista";
        }catch (MiException ex) {
            model.put("error", ex.getMessage());
            return "autor_modificar.html";
        }
        
    }
}

