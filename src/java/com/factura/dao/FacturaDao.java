/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.factura.dao;

import com.factura.dbutil.ConectarBD;
import com.factura.model.Factura;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Juan Pablo Beltran
 */
public class FacturaDao {
    
    private ConectarBD conectarBD; 
    
    public FacturaDao()throws Exception{
        conectarBD = new ConectarBD("127.0.0.1","bdfacturas","root","admin");
    }
    
    public void insertarFactura(Factura factura){
        try{
        String sentencia =new String();
 	sentencia = "Insert Into Facturas Values("+factura.getIdFactura()+","+factura.getNumeroFactura()+",'"+factura.getFecha()+"','"+factura.getTipodePago()+"', '"+factura.getDocumentoCliente()+"','"+factura.getNombreCliente()+"',"+factura.getSubTotal()+","+factura.getDescuento()+","+factura.getIva()+","+factura.getTotalDescuento()+","+factura.getTotalImpuesto()+","+factura.getTotal()+")";
 	conectarBD.getSentencia().execute(sentencia);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void eliminarFactura(int numeroFactura){
        try{
        String sentencia =new String();
 	sentencia = "Delete From Facturas Where numeroFactura="+numeroFactura+"";
 	conectarBD.getSentencia().execute(sentencia);
        }catch(SQLException e){
            e.printStackTrace();
        }        
    }
    
    public List<Factura> getAllFacturas() {
        List<Factura> facturas = new ArrayList<Factura>();
        try {
            ResultSet rs = conectarBD.buscarRegistro("select * from facturas");
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setIdFactura(rs.getInt("idFactura"));
                factura.setNumeroFactura(rs.getInt("numeroFactura"));
                factura.setFecha(rs.getString("fecha"));
                factura.setTipodePago(rs.getString("tipodePago"));
                factura.setDocumentoCliente(rs.getString("documentoCliente"));
                factura.setNombreCliente(rs.getString("nombreCliente"));
                factura.setSubTotal(rs.getInt("subTotal"));
                factura.setDescuento(rs.getInt("descuento"));
                factura.setIva(rs.getInt("iva"));
                factura.setTotalDescuento(rs.getFloat("totalDescuento"));
                factura.setTotalImpuesto(rs.getFloat("totalImpuesto"));
                factura.setTotal(rs.getFloat("total"));
                facturas.add(factura);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return facturas;
    }
    
    public void actualizarFactura(Factura factura){
        String sql = null;
        try {
            sql = "Update Facturas "+
                  "Set fecha='"+factura.getFecha()+"',tipodePago='"+factura.getTipodePago()+"',documentoCliente='"+factura.getDocumentoCliente()+"',nombreCliente='"+factura.getNombreCliente()+"',subTotal="+factura.getSubTotal()+",descuento="+factura.getDescuento()+",iva="+factura.getIva()+",totalDescuento="+factura.getTotalDescuento()+",totalImpuesto="+factura.getTotalImpuesto()+", total="+factura.getTotal()+
                  " Where numeroFactura="+factura.getNumeroFactura()+"";
 	    conectarBD.getSentencia().execute(sql); 
        } catch (Exception e) {
            e.printStackTrace();
        }                                        
    }

}
