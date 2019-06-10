package org.graph;

import lombok.Getter;

/**
 * 图的边
 */
@Getter
public class Edge {
    Node start;
    Node end;
    public Edge(Node s, Node e){
        start = s;
        end = e;
    }
}
