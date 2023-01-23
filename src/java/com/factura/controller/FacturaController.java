/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.factura.controller;


import com.factura.dao.FacturaDao;
import com.factura.model.Factura;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.context.RequestContext;
/**
 *
 * @author Juan Pablo Beltran
 */
@ManagedBean
@RequestScoped
public class FacturaController{
    
    private FacturaDao dao;
    private Factura factura;
    
    public FacturaController()throws Exception{
        dao = new FacturaDao();
    }
    
    public Factura getObjeto(){
        if(factura==null)factura = new Factura();
        return factura;
    }
    
    /**
     * Metodo que obtiene lista de facturas
     */
    public List<Factura> listarFacturas()throws Exception{
        dao = new FacturaDao();
        return dao.getAllFacturas();
    } 
    
    /**
     * Metodo que inserta una factura 
     */
    public String insertarFactura(Factura obj){
        try{
            dao = new FacturaDao();
            dao.insertarFactura(obj);
         }catch(Exception e){
            e.printStackTrace();
        }
        return "Factura Insertada Correctamente"; 
    }

    /**
     * Metodo eliminar factura
     */
    public String eliminarFactura(int id){
        try{
            dao = new FacturaDao();
            dao.eliminarFactura(id);
         }catch(Exception e){
            e.printStackTrace();
        }
        return "Factura eliminada Correctamente";
    }  
    
    /**
     * Metodo que actualiza una factura 
     */
    public String actualizarFactura(Factura obj){
        try{
            dao = new FacturaDao();
            dao.actualizarFactura(obj);
         }catch(Exception e){
            e.printStackTrace();
        }
        return "Factura Actualizada Correctamente"; 
    }    
    
    public void crear(){
       RequestContext.getCurrentInstance().update("frmCrear:panelCrear");
       RequestContext.getCurrentInstance().execute("PF('dialogoCrear').show()");        
    }    
}
