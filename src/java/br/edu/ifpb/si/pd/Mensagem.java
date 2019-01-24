/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.si.pd;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 *
 * @author Alexandre
 */
public class Mensagem {
    private String remetente;
    private String destinatario;
    private String conteudo;
    private boolean enviada;
    private String dataAtual;

    public Mensagem() {
        GregorianCalendar data = new GregorianCalendar();
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	String dataFormatada = formato.format(data.getTime());
        this.dataAtual = dataFormatada;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public String getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(String dataAtual) {
        this.dataAtual = dataAtual;
    }
    
    
    
}
