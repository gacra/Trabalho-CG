/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho.cg;

import com.sun.glass.events.MouseEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.event.MouseInputListener;

public class Renderer implements  GLEventListener, MouseInputListener, KeyListener{
    private final GLCanvas canvas;
    PreePol preePol;
    private GL gl;
    ArrayList<Pair<Integer, Integer>> listaPontos;
    int x=-1, y=-1;
    boolean flagPree;
    int linha;
    float red, green, blue;

    public Renderer(GLCanvas canvas, PreePol preePol){
        this.canvas = canvas;
        this.preePol = preePol;
        listaPontos = new ArrayList<>();
        flagPree = false;
        red = 1.0f;
        green = 0.0f;
        blue = 0.0f;
    }
    
    @Override
    public void init(GLAutoDrawable drawable){
        gl = drawable.getGL();
        
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); //Cor de fundo
        gl.glMatrixMode(GL.GL_PROJECTION); //Carrega a matriz de projeção
        gl.glLoadIdentity();
        
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0, 500.0, 0.0, 500.0); //Projeção ortogonal 2D 
    }

    @Override
    public void display(GLAutoDrawable drawable){
            
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(red, green, blue); //ALtera atributo de cor        
        
        if(flagPree){
            LinkedList<Pair<Integer, Integer>> listaPixels;
            
            while(true){            
                listaPixels = preePol.preencher();
                if(listaPixels == null){ break;}
                for(Pair<Integer, Integer> p: listaPixels){
                    gl.glPointSize(1);
                    gl.glBegin(GL.GL_POINTS);
                    for(int i=p.getKey(); i<=p.getValue(); i++){
                        gl.glVertex2i(i, linha);
                    }
                    gl.glEnd();  
                }
                linha++;
            }
        }else{
            gl.glPointSize(10);
            gl.glBegin(GL.GL_POINTS);
            for(Pair<Integer, Integer> p : listaPontos){
                gl.glVertex2i(p.getKey(), p.getValue());
            }
            gl.glEnd();
        }
        
        gl.glFlush();
    }

        @Override
    public void mouseClicked(java.awt.event.MouseEvent e){
        if(flagPree){return;}
        x = e.getX();
        y = 500-e.getY();
        if(jaExiste(x, y)){return;}
        listaPontos.add(new Pair<>(x, y));
        preePol.addPonto(x, y);
        preePol.imprimeET();
        canvas.display();
    }
    
    @Override
    public void keyTyped(KeyEvent e){
    }
    
    public boolean preencher(){
        if(!flagPree){
            if(temPontos()){
                linha = preePol.termina();
                preePol.imprimeET();
                flagPree = true;
                listaPontos.clear();
                canvas.display();
            }else{
                return flagPree;
            }
        }else{
            flagPree = false;
            preePol.reinicia();
            canvas.display();
        }
        return flagPree;
    }
    
    private boolean temPontos(){
        return (listaPontos.size()>2);
    }
    
    private boolean jaExiste(int x, int y){
        for(Pair<Integer, Integer> p : listaPontos){
            if (p.getKey()==x && p.getValue()==y){
                return true;
            }
        }
        return false;
    }
    
    public void setCor(int red, int green, int blue){
        this.red = red/255.0f;
        this.green = green/255.0f;
        this.blue = blue/255.0f;
        canvas.display();
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){}

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged){}

    @Override
    public void mousePressed(java.awt.event.MouseEvent e){}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e){}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e){}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e){}

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e){}

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e){}  

    @Override
    public void keyPressed(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}
}
