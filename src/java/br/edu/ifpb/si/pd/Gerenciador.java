/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.si.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Alexandre
 */
@ServerEndpoint("/operadora/{numero}")
public class Gerenciador {
    private static final List<Usuario> usuarios = Collections.synchronizedList(new ArrayList<Usuario>());
    private Usuario usuario;   
    
    @OnOpen
    public void abrir(Session ses, @PathParam("numero")String numero){
        boolean flag = true;
        Mensagem msgLida = new Mensagem();
        for (Usuario user: usuarios){
            if(user.getNumero().equals(numero)){
                if(user.getMensagem()!= null){
                    if(!user.getMensagem().isEnviada()){
                        //System.out.println("Mensagem não enviada");                        
                        msgLida.setEnviada(true);
                        try {
                            ses.getBasicRemote().sendText("Remetente: "+user.getMensagem().getRemetente()+
                                                      "\nData: "+user.getMensagem().getDataAtual()+
                                                      "\n--> "+user.getMensagem().getConteudo());
                        } catch (IOException ex) {
                            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        user.getMensagem().setEnviada(true);
                        for(Usuario uses: usuarios){                        
                            if(uses.getNumero().equals(user.getMensagem().getRemetente())&& uses.getSessao().isOpen()){
                                //System.out.println("Chegou aqui");
                                uses.setNotificacao(msgLida);                                
                                if(msgLida.isEnviada()){
                                    try {
                                        uses.getSessao().getBasicRemote().sendText("Mensagem entregue\nData:"+msgLida.getDataAtual());                                        
                                    } catch (IOException ex) {
                                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                                    }                              
                                }else {
                                    uses.getNotificacao().setEnviada(false);                                                                        
                                }                                
                            }
                        }
                    }                    
                }                
                user.setSessao(ses);
                this.usuario = user;
                flag = false;                
                /*if(user.getNotificacao()!=null){
                    if(!user.getNotificacao().isEnviada()){
                        System.out.println("Chegou aqui");
                        try {
                            ses.getBasicRemote().sendText("Mensagem entregue\nData:"+user.getNotificacao().getDataAtual());
                        } catch (IOException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                    
                }*/
                
                if(user.getNotificacao()!=null){                  
                    if(!user.getNotificacao().isEnviada()){                        
                        try {
                            ses.getBasicRemote().sendText("Mensagem entregue\nData:"+user.getNotificacao().getDataAtual());
                        } catch (IOException ex) {
                            Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }           
        }
        if(flag){
            try {
                ses.getBasicRemote().sendText("Numero registrado " + numero);
                Usuario user = new Usuario();
                user.setSessao(ses);
                user.setNumero(numero);
                user.setSaldo(0);           
                usuarios.add(user);
                this.usuario = user;
                //usuarios.add(ses);
            }catch (IOException ex) {
                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
    
    @OnMessage
    public void onMessage(String message, @PathParam("numero")String numero, Session ses) {
        Usuario usuarioDest = new Usuario();
        Mensagem msg = new Mensagem();
        
        if(message.startsWith("send")){        
        
        if(usuario.getSaldo() > 0){
        List<String> msgLista = Arrays.asList(message.split(" "));
        
        String msgSozinha = "";
        for(int i = 2; i < msgLista.size(); i++){
            msgSozinha = msgSozinha+" "+msgLista.get(i);
        } 
        
        msg.setConteudo(msgSozinha);
        msg.setRemetente(numero);
        
        for(Usuario user: usuarios){           
            if(user.getNumero().equals(msgLista.get(1)) && !user.getNumero().equals(numero)){
                for(Usuario uses: usuarios){
                    if(uses.getNumero().equals(numero)){
                        uses.setSaldo((float) (uses.getSaldo() - 0.5));
                    }
                }
                if(!user.getSessao().isOpen()){
                    //System.out.println("Sessão está fechada...");
                    user.setMensagem(msg);
                    user.getMensagem().setEnviada(false);
                }else{
                    if(user.getNumero().equals(numero)){
                       
                    }
                    try {                   
                        user.getSessao().getBasicRemote().sendText("Remetente: "+numero
                            +"\nData: "+msg.getDataAtual()
                            +"\n-->"+msg.getConteudo());
                        user.setMensagem(msg);
                        user.getMensagem().setEnviada(true);
                    
                    }catch (IOException ex) {
                        Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                }                
                usuarioDest.setSessao(user.getSessao());
                
            }else if(usuario.getNumero().equals(msgLista.get(1)) && user.getNumero().equals(numero)){
                try {
                    ses.getBasicRemote().sendText("Número de telefone igual ao do rementente");
                } catch (IOException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(!user.getNumero().equals(msgLista.get(1)) && !user.getSessao().equals(ses)){
                try {
                    ses.getBasicRemote().sendText("Número de telefone não existe");
                }catch (IOException ex) {
                    Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
          }
        }else{
            try {
                ses.getBasicRemote().sendText("Saldo insuficiente...");
            } catch (IOException ex) {
                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        
        }else if(message.startsWith("creditar")){
            List<String> msgLista = Arrays.asList(message.split(" "));           
            float novoSaldo = Float.parseFloat(msgLista.get(1));
            for (Usuario user: usuarios){
                if(user.getNumero().equals(numero)){
                    user.setSaldo(user.getSaldo()+novoSaldo);
                    usuario.setSaldo(user.getSaldo());
                }
            }
            
        }else if(message.startsWith("saldo")){
            try {
                ses.getBasicRemote().sendText("Saldo: "+usuario.getSaldo());
            } catch (IOException ex) {
                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                ses.getBasicRemote().sendText("Comando inválido...");
            } catch (IOException ex) {
                Logger.getLogger(Gerenciador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
    
    @OnClose
    public void sair(Session ses){
        //usuarios.remove(ses);
    }
    
    public void notificacao(){
        
    }
    
}
