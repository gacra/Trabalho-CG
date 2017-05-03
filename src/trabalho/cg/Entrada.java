package trabalho.cg;

/**
 * Entrada ("caixa") das tabelas ET e AET
 * Representa uma aresta
 */
public class Entrada implements Cloneable{
   int y_max;       //y máximo da aresta
   //x (inicia mínimo e é incrementado)
   int x_int;       //Parte inteira do x
   int x_num;       //Numerador da parte fracionária do x
                    //Obs: o denominador é igual ao de m_inv
   //Inverso da inclinação da aresta
   int m_inv_num;   //Numerador
   int m_inv_den;   //Denominador

    public Entrada(int y_max, int x_int, int m_inv_num, int m_inv_den){
        this.y_max = y_max;
        this.x_int = x_int;
        this.x_num = 0;
        //Caso m_inv seja negativo, o sinal deve ficar no numerador.
        if(m_inv_den < 0){
            this.m_inv_num = - m_inv_num;
            this.m_inv_den = - m_inv_den;
        }else{
            this.m_inv_num =  m_inv_num;
            this.m_inv_den =  m_inv_den;
        }            
    }

    public int getX_int(){
        return x_int;
    }

    /**
     * Atualiza o x da aresta pelo algoritmo incremental
     */
    void atualiza(){

        x_num += m_inv_num;
        
        while(x_num>=m_inv_den){
            x_num -= m_inv_den;
            x_int++;
        }
        
        while(x_num<0){
            x_num += m_inv_den;
            x_int --;
        }  
    }
    
    /**
     * Arredonda o valor de x para cima
     * @return Valor de x arredondado para cima
     */
    public  int x_arredonda(){
        if(x_num == 0){
            return x_int;
        }else{
            return  x_int+1;
        }
    }
    
   @Override
    public Entrada clone(){
       try{
           return (Entrada) super.clone();
       }catch(CloneNotSupportedException ex){
          return null;
       }
    }
}
