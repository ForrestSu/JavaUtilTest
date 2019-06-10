package org.graph;


import lombok.Getter;
import lombok.Setter;

/**
 *  图的节点
 */
@Getter
@Setter
public class Node {
    long id;
    String nodeName;
    int indegree;
    int outdegree;

}
