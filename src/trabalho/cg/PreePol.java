package trabalho.cg;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javafx.util.Pair;

public class PreePol{
    private HashMap<Integer, LinkedList<Entrada>> edgeTable;
    private LinkedList<Entrada> activeEdgeTable;
    int x_primeiro, y_primeiro;
    int x_anterior, y_anterior;
    int linVar;
    Comparator<Entrada> comparador;

    public PreePol(){
        edgeTable = new HashMap<>();
        activeEdgeTable = new LinkedList<>();
        x_primeiro = -1;
        comparador = new Comparator<Entrada>() {

            @Override
            public int compare(Entrada o1, Entrada o2){
                if(o1.getX() == o2.getX()){
                    return 0;
                }else if(o1.getX() > o2.getX()){
                    return 1;
                }else{
                    return -1;
                }
            }
        };
    }
    
    public boolean addPonto(int x, int y){
        if(x_primeiro == -1){   //Primeiro ponto
            x_primeiro = x_anterior = x;
            y_primeiro = y_anterior = y;
            return true;
        }
        if(y != y_anterior){
            int y_min, y_max;
            float x_min, m_inv;
            
            if(x<x_anterior){
                m_inv = (float)(x_anterior - x)/(float)(y_anterior-y);
            }else{
                m_inv = (float)(x - x_anterior) / (float)(y - y_anterior);
            }
            
            if(y<y_anterior){
                y_min = y;
                x_min = x;
                y_max = y_anterior;
            }else{
                y_min = y_anterior;
                x_min = x_anterior;
                y_max = y;
            }
            
            if(edgeTable.containsKey(y_min)){
                edgeTable.get(y_min).add(new Entrada(y_max, x_min, m_inv));
                Collections.sort(edgeTable.get(y_min), comparador);
            }else{
                LinkedList<Entrada> novaLista = new LinkedList<>();
                novaLista.add(new Entrada(y_max, x_min, m_inv));
                edgeTable.put(y_min, novaLista);
            }
        }
                    
        x_anterior = x;
        y_anterior = y;
        
        return true;
    }
    
    public void imprimeET(){
        System.out.println("Edge Table:\n");
        Set<Integer> chaves = edgeTable.keySet();
        for(Integer chave : chaves){
            System.out.println("Linhas de varredura: " + chave);
            LinkedList<Entrada> lista = edgeTable.get(chave);
            for(Entrada ent : lista) { //percorrer toda a lista até o ultimo elemento
                System.out.println(ent);
            }        
        }   
    }
    
    public int termina(){
        addPonto(x_primeiro, y_primeiro);
        Set<Integer> chaves = edgeTable.keySet();
        Iterator<Integer> it = chaves.iterator();
        linVar = 500;
        while(it.hasNext()){
            Integer next = it.next();
            if(next < linVar){
                linVar = next;
            }
        }
        return linVar;
    }
    
    public LinkedList<Pair<Integer, Integer>> preencher(){
        //System.out.println("PreePol: " + linVar);
        //Repita até a ET e a AET estarem vazias
        if(edgeTable.isEmpty() && activeEdgeTable.isEmpty()){
            //System.out.println("Saindo: " + linVar);
            return null;
        }
        
        //Transfere as arestas da linha de varredura da ET para a AET
        //mantendo a ordenação em x.
        if(edgeTable.containsKey(linVar)){
            LinkedList<Entrada> lista = edgeTable.get(linVar);
            for(Entrada e: lista){
                activeEdgeTable.add(e);
            }
            edgeTable.remove(linVar);
            Collections.sort(activeEdgeTable, comparador);
        }
        //imprimeAET();
        //Retira os lados que possuem y_max = linha de varredura.
        Iterator itr = activeEdgeTable.iterator();
        while (itr.hasNext()) {
            if(((Entrada)itr.next()).y_max==linVar){
                itr.remove();
            }
        }
        //imprimeAET();
        //Desenhe os pixels
        LinkedList<Pair<Integer, Integer>> listaPixels = 
                new LinkedList<>();
        int x_ini, x_fim;
        Iterator<Entrada> it = activeEdgeTable.iterator();
        for(int i=0; i<activeEdgeTable.size(); i+=2){
            x_ini = (int) Math.ceil((double)(it.next()).x);
            x_fim = ((int) Math.ceil((double)(it.next()).x))-1; 
            //System.out.println("X_ini, x_fim" + x_ini + "," + x_fim);
            listaPixels.add(new Pair<>(x_ini, x_fim));
        }
        
        //Incrementa a linha de varredura
        linVar++;
        
        //Para cada aresta não vertical atualiza x para nova linha de varredura
        for(Entrada e : activeEdgeTable){
            e.atualiza();
        }
        
        //Reordenar a AET
        Collections.sort(activeEdgeTable, comparador);
        
        //imprimeAET();
        
        return listaPixels;
    }
    
    public void imprimeAET(){
        System.out.println("AET: ");
        for(Entrada e: activeEdgeTable){
            System.out.println(e.toString());
        }
    }
    
    public void reinicia(){
        x_primeiro = -1;
    }
    
}
