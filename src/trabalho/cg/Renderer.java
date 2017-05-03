package trabalho.cg;

import java.util.ArrayList;
import java.util.LinkedList;
import javafx.util.Pair;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.event.MouseInputListener;

/**
 * Responsável pela renderização dos objetos.
 */
public class Renderer implements  GLEventListener, MouseInputListener{
    private final GLCanvas canvas;  //Canvas do OpenGL
    private GL gl;
    PreePol preePol;
    ArrayList<Pair<Integer, Integer>> listaPontos; //Lista de pontos adicionados
    boolean flagPree;       //true se o polígono estiver preenchido
    int linha;              //Linha de varredura atual
    float red, green, blue; //Cor dos objetos

    public Renderer(GLCanvas canvas, PreePol preePol){
        this.canvas = canvas;
        this.preePol = preePol;
        listaPontos = new ArrayList<>();
        flagPree = false;
        //Vermelho
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

    /**
     * Desenha os objetos
     */
    @Override
    public void display(GLAutoDrawable drawable){
            
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(red, green, blue); //ALtera atributo de cor        
        
        //Se for para preencher o polígono, chama os métodos de PreePol
        //para receber a lista de pixels a serem pintados para cada
        //linha de varredura, e pinta tais pixels.
        if(flagPree){
            linha = preePol.prepara();
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
        }else{  //Não preenche o polígono, apenas desenha os pontos selecionados.
            gl.glPointSize(10);
            gl.glBegin(GL.GL_POINTS);
            for(Pair<Integer, Integer> p : listaPontos){
                gl.glVertex2i(p.getKey(), p.getValue());
            }
            gl.glEnd();
        }
        
        gl.glFlush();
    }

     /**
     * Recebe as coordenadas da tela, converte em coordenadas do canvas,
     * e adiciona o ponto na PreePol.
     */
    @Override
    public void mousePressed(java.awt.event.MouseEvent e){
        if(flagPree){return;}
        int x = e.getX();
        int y = 500-e.getY();
        if(jaExiste(x, y)){return;}
        listaPontos.add(new Pair<>(x, y));
        preePol.addPonto(x, y);
        canvas.display();
    }
    
    /**
     * Preparação para o preenchimento do polígono e chamada de Display().
     * @return True: se foi possível preencher o polígono.
     * False: se não foi possível preencher o polígono (há menos de 3 pontos
     * distintos selecionados).
     */
    public boolean preencher(){
        if(!flagPree){
            if(temPontos()){
                preePol.termina();
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
    
    /**
     * Verifica de há pontos suficiente para criação de um polígono.
     * @return True: é possível criar um polígono.
     * False: não é possível criar um polígono (há menos de 3 pontos
     * distintos selecionados).
     */
    private boolean temPontos(){
        return (listaPontos.size()>2);
    }
    
    /**
     * Verifica se o ponto já havia sido selecionado antes.
     * @param x Coordenada x do ponto
     * @param y Coordenada y do ponto
     * @return True: o ponto já existe (já foi selecionado antes)
     * False: o ponto ainda não existe (não foi selecionado ainda)
     */
    private boolean jaExiste(int x, int y){
        for(Pair<Integer, Integer> p : listaPontos){
            if (p.getKey()==x && p.getValue()==y){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Muda a cor dos objetos.
     * @param red Vermelho no RGB (0-255)
     * @param green Verde no RGB (0-255)
     * @param blue Azul do RGB (0-255)
     */
    public void setCor(int red, int green, int blue){
        this.red = red/255.0f;
        this.green = green/255.0f;
        this.blue = blue/255.0f;
        canvas.display();
    }
    
    //Métodos sobrescritos mas não usados
        
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e){}
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){}

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged){}

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

}
