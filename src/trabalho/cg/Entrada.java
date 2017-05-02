package trabalho.cg;

public class Entrada{
   int y_max;
   float x;
   float m_inv;

    public Entrada(int y_max, float x, float m_inv){
        this.y_max = y_max;
        this.x = x;
        this.m_inv = m_inv;
    }

    public int getY_max(){
        return y_max;
    }

    public float getX(){
        return x;
    }

    public float getM_inv(){
        return m_inv;
    }

    public void setY_max(int y_max){
        this.y_max = y_max;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setM_inv(float m_inv){
        this.m_inv = m_inv;
    }

    @Override
    public String toString(){
        return "Entrada{" + "y_max=" + y_max + ", x=" + x + ", m_inv=" + m_inv + "}";
    }

    void atualiza(){
        x += m_inv;
    }
    
    
}
