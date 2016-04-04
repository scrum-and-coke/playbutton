package application.musicplayer.muse;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ben on 4/3/2016.
 */
public class Clusterer {

    public static class MusicLocation{
        Location loc;
        ArrayList<String> genres;
        MusicLocation(Location l, ArrayList<String> g){
            loc = l;
            genres = g;
        }
    }

    private static class Node {
        public MusicLocation val;
        boolean visited;
        boolean clustered;

        Node(MusicLocation ml) {
            val = ml;
            visited = false;
            clustered = false;
        }
    }

    private HashMap<Node, LinkedList<Node>> _graph;
    private int _minPts;

    public Clusterer(List<MusicLocation> dataset) {
        _graph = new HashMap<Node, LinkedList<Node>>(dataset.size());
    }

    List<List<MusicLocation>> DBSCAN(List<MusicLocation> dataset, float eps, int minPts) {

        for (int i = 0; i < dataset.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                if (dataset.get(i).loc.distanceTo(dataset.get(j).loc) > eps) {
                    Node iNode = new Node(dataset.get(i));
                    Node jNode = new Node(dataset.get(j));
                    _graph.put(iNode, new LinkedList<Node>());
                    _graph.get(iNode).add(jNode);
                    _graph.put(jNode, new LinkedList<Node>());
                    _graph.get(jNode).add(iNode);
                }
            }
        }

        List<List<MusicLocation>> clusters = new ArrayList<List<MusicLocation>>();
        ArrayList<MusicLocation> c = new ArrayList<MusicLocation>();
        for (Node n : _graph.keySet()){
            if (n.visited) continue;
            n.visited = true;
            List<Node> neighbourPts = _graph.get(n);
            if (neighbourPts.size() >= minPts) {
                expandCluster(n, neighbourPts, c, minPts);
                clusters.add(c);
                c = new ArrayList<MusicLocation>();
            }
        }

        return clusters;
    }

    void expandCluster(Node point, List<Node> neighbourhood, List<MusicLocation> cluster, int minPts) {
        cluster.add(point.val);
        point.clustered = true;
        for (Node n : neighbourhood) {
            if (!n.visited) {
                n.visited = true;
                List<Node> nextNeighbourhood = _graph.get(n);
                if (nextNeighbourhood.size() >= minPts) neighbourhood.addAll(nextNeighbourhood);
            }
            if (!n.clustered) {
                cluster.add(n.val);
                n.clustered = true;
            }
        }
    }

}
