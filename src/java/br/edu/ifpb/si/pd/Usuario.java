/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.si.pd;

import javax.websocket.Session;

/**
 *
 * @author Alexandre
 */
public class Usuario {
    private Session sessao;
    private String numero;
    private float saldo;
    private Mensagem mensagem;
    private Mensagem notificacao;

    public Usuario() {
    }    

    public Session getSessao() {
        return sessao;
    }

    public void setSessao(Session sessao) {
        this.sessao = sessao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }    

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public Mensagem getNotificacao() {
        return notificacao;
    }

    public void setNotificacao(Mensagem notificacao) {
        this.notificacao = notificacao;
    }     

    @Override
    public String toString() {
        return "Usuario{" + "sessao=" + sessao + ", numero=" + numero + ", saldo=" + saldo + ", mensagem=" + mensagem.getConteudo() + '}';
    }
   
}
