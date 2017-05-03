package trabalho.cg;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javafx.util.Pair;

/**
 * Classe responsável pelo algoritmo de Preenchimento de Polígonos 
 */
public class PreePol{
    //Tabela de Lados (EdgeTable - ET) e sua cópia
    private HashMap<Integer, LinkedList<Entrada>> edgeTable, edgeTable_copia;
    //Tabela de Lados Ativos (Active Edge Table - AET)
    private LinkedList<Entrada> activeEdgeTable;
    int x_primeiro, y_primeiro; //Coordenadas do primeiro ponto
    int x_anterior, y_anterior; //Coordenadas do último ponto inserido    
    int linVar; //Linha de varredura atual
    Comparator<Entrada> comparador; //Comparador usado na ordenação

    public PreePol(){
        edgeTable = new HashMap<>();
        activeEdgeTable = new LinkedList<>();
        x_primeiro = -1;
        comparador = new Comparator<Entrada>() {

            @Override
            public int compare(Entrada o1, Entrada o2){
                if(o1.getX_int()== o2.getX_int()){
                    return 0;
                }else if(o1.getX_int()> o2.getX_int()){
                    return 1;
                }else{
                    return -1;
                }
            }
        };
    }
    
    /**
     * Adiciona um novo ponto.
     * A partir do segundo ponto adicionado, a aresta formada por esse 
     * ponto e o anterior a ele é adicionada na ET.
     * @param x Coordenada x do ponto
     * @param y Coordenada y do ponto
     */
    public void addPonto(int x, int y){
        if(x_primeiro == -1){   //Primeiro ponto
            x_primeiro = x_anterior = x;
            y_primeiro = y_anterior = y;
        }
        if(y != y_anterior){    //Não insere arestas horizontais
            int y_min, y_max;
            float x_min, m_inv;
            int num, den;
            
            if(x<x_anterior){
                m_inv = (float)(x_anterior - x)/(float)(y_anterior-y);
                num = x_anterior - x;
                den = y_anterior-y;
            }else{
                m_inv = (float)(x - x_anterior) / (float)(y - y_anterior);
                num = x - x_anterior;
                den = y - y_anterior;
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
                edgeTable.get(y_min).add(new Entrada(y_max, (int)x_min,num, den));
                Collections.sort(edgeTable.get(y_min), comparador);
            }else{
                LinkedList<Entrada> novaLista = new LinkedList<>();
                novaLista.add(new Entrada(y_max, (int)x_min,num, den));
                edgeTable.put(y_min, novaLista);
            }
        }
                    
        x_anterior = x;
        y_anterior = y;
    }
    
    /**
     * "Fecha" o polígono, criando uma aresta entre o último ponto adicionado
     * e o primeiro.
     */
    public void termina(){
        addPonto(x_primeiro, y_primeiro);
    }
    
    /**
     * Prepara os objetos para o loop de manipulação da AET.
     * Chamada antes de Preencher().
     * Cria uma cópia da ET, para caso o algoritmo seja rodado novamente.
     * @return Menor linha de varredura não vazia da ET.
     */
    public int prepara(){
        Set<Integer> chaves = edgeTable.keySet();
        Iterator<Integer> it = chaves.iterator();
        linVar = 500;
        while(it.hasNext()){
            Integer next = it.next();
            if(next < linVar){
                linVar = next;
            }
        }
        copia();
        return linVar;
    }
    
    /**
     * Loop de manipulação da AET com retorno dos intervalos de pixels a
     * serem pintados.
     * Cada chamada da função corresponde a apenas uma volta do loop.
     * @return Lista de intervalos de pixels a serem pintados na
     * linha de varredura atual. 
     * Null caso não haja mais pixels a serem pintados.
     */
    public LinkedList<Pair<Integer, Integer>> preencher(){
        //Repita até a ET e a AET estarem vazias
        if(edgeTable_copia.isEmpty() && activeEdgeTable.isEmpty()){
            //System.out.println("Saindo: " + linVar);
            return null;
        }
        
        //Transfere as arestas da linha de varredura da ET para a AET
        //mantendo a ordenação em x.
        if(edgeTable_copia.containsKey(linVar)){
            LinkedList<Entrada> lista = edgeTable_copia.get(linVar);
            for(Entrada e: lista){
                activeEdgeTable.add(e);
            }
            edgeTable_copia.remove(linVar);
            Collections.sort(activeEdgeTable, comparador);
        }

        //Retira os lados que possuem y_max = linha de varredura.
        Iterator itr = activeEdgeTable.iterator();
        while (itr.hasNext()) {
            if(((Entrada)itr.next()).y_max==linVar){
                itr.remove();
            }
        }

        //Desenhe os pixels
        LinkedList<Pair<Integer, Integer>> listaPixels = 
                new LinkedList<>();
        int x_ini, x_fim;
        Iterator<Entrada> it = activeEdgeTable.iterator();
        for(int i=0; i<activeEdgeTable.size(); i+=2){
            x_ini = it.next().x_arredonda();
            x_fim = it.next().x_arredonda() - 1;
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
        
        return listaPixels;
    }

    /**
     * Reinicia o algoritmo.
     * Limpa a ET e se prepara pra receber os pontos novos.
     */
    public void reinicia(){
        edgeTable.clear();
        x_primeiro = -1;
    }
    
    /**
     * Faz uma cópia da ET.
     */
    public void copia(){
        edgeTable_copia = new HashMap<>();
        LinkedList<Entrada> copiaList;
        Set<Integer> chaves = edgeTable.keySet();
        for(Integer chave : chaves){
            copiaList = new LinkedList<>();
            for(Entrada e: edgeTable.get(chave)){
                copiaList.add((Entrada) e.clone());
            }
            edgeTable_copia.put(chave, copiaList);
        }      
    }
    
}
