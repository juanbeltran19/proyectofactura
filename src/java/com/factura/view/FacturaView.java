/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.factura.view;

import java.util.Scanner;
import java.util.InputMismatchException;
import com.factura.controller.FacturaController;
import com.factura.model.Factura;
import java.util.List;

/**
 *
 * @author Juan Pablo Beltran
 */
public class FacturaView {
    
    public static void main(String[] args) throws Exception {
 
        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion; //Guardaremos la opcion del usuario
        FacturaController facturaController = new FacturaController();
        Factura factura = new Factura();
        
        while (!salir) {
 
            System.out.println("1. Listar Factura");
            System.out.println("2. Crear Factura");
            System.out.println("3. Actualizar Factura");
            System.out.println("4. Eliminar Factura");
            System.out.println("5. Salir");
 
            try {
 
                System.out.println("Escribe una de las opciones");
                opcion = sn.nextInt();
 
                switch (opcion) {
                    case 1:
                        System.out.println("Lista de Facturas");
                        List<Factura> listarFactura = facturaController.listarFacturas();
                        for(Factura lista: listarFactura){
                            System.out.println("IdFactura:"+lista.getIdFactura());
                            System.out.println("Numero Factura:"+lista.getNumeroFactura());
                            System.out.println("Fecha:"+lista.getFecha());
                            System.out.println("Tipo de Pago:"+lista.getTipodePago());
                            System.out.println("Documento Cliente:"+lista.getDocumentoCliente());
                            System.out.println("Nombre Cliente:"+lista.getNombreCliente());
                            System.out.println("SubTotal:"+lista.getSubTotal());
                            System.out.println("Descuento:"+lista.getDescuento());
                            System.out.println("Iva:"+lista.getIva());
                            System.out.println("Total Descuento:"+lista.getTotalDescuento());
                            System.out.println("Total Impuesto:"+lista.getTotalImpuesto());
                            System.out.println("Total:"+lista.getTotal());        
                        }
                        break;
                    case 2:
                          System.out.println("Ingresar Factura");
                            /*System.out.println("Ingrese IdFactura:");
                            factura.setIdFactura(sn.nextInt());
                            System.out.println("Ingrese Numero Factura:");
                            factura.setNumeroFactura(sn.nextInt());
                            System.out.println("Fecha:");
                            factura.setFecha(sn.next());
                            System.out.println("Tipo de Pago:");
                            factura.setTipodePago(sn.next());
                            System.out.println("Documento Cliente:");
                            factura.setDocumentoCliente(sn.next());
                            System.out.println("Nombre Cliente:");
                            factura.setNombreCliente(sn.next());
                            System.out.println("SubTotal:");
                            factura.setSubTotal(sn.nextInt());
                            System.out.println("Descuento:");
                            factura.setDescuento(sn.nextInt());
                            System.out.println("Iva:");
                            factura.setIva(sn.nextInt());
                            System.out.println("Total Descuento:");
                            factura.setTotalDescuento(sn.nextFloat());
                            System.out.println("Total Impuesto:");
                            factura.setTotalImpuesto(sn.nextFloat());
                            System.out.println("Total:");
                            factura.setTotal(sn.nextFloat());*/
                            factura.setIdFactura(3);
                            factura.setNumeroFactura(1004);
                            factura.setFecha("11/12/2018");
                            factura.setTipodePago("Contado");
                            factura.setDocumentoCliente("80225666");
                            factura.setNombreCliente("Mario Duarte");
                            factura.setSubTotal(300000);
                            factura.setDescuento(5);
                            factura.setIva(19);
                            factura.setTotalDescuento(15000);
                            factura.setTotalImpuesto(54150);
                            factura.setTotal(339150);
                            
                            System.out.println(facturaController.insertarFactura(factura));
                        break;
                    case 3:
                        System.out.println("Actualizar Factura");
                        
                            factura.setIdFactura(3);
                            factura.setNumeroFactura(1004);
                            factura.setFecha("11/12/2018");
                            factura.setTipodePago("Credito");
                            factura.setDocumentoCliente("92548165");
                            factura.setNombreCliente("Juan Beltran");
                            factura.setSubTotal(600000);
                            factura.setDescuento(5);
                            factura.setIva(19);
                            factura.setTotalDescuento(30000);
                            factura.setTotalImpuesto(100000);
                            factura.setTotal(730000);
                            System.out.println(facturaController.actualizarFactura(factura));
                        break;
                    case 4:
                        System.out.println("Eliminar Factura");
                        System.out.println("Ingrese el numero de factura:");
                        System.out.println(facturaController.eliminarFactura(sn.nextInt()));
                        break;
                     case 5:
                        salir = true;
                        break;                       
                    default:
                        System.out.println("Solo números entre 1 y 5");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                sn.next();
            }
        }
 
    }
    
}
