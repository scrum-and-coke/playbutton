package application.musicplayer.muse;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Ben on 4/3/2016.
 */
public class Clusterer {

    public static class MusicLocation {
        public Location loc;
        public Playlist playlist;
        MusicLocation(Location l, Playlist pl){
            loc = l;
            playlist = pl;
        }
    }

    private static MusicLocation mergeCluster(List<MusicLocation> locations) {
        double minLat = 999999999.9;
        double minLong = 999999999.9;
        double maxLat = 0;
        double maxLong = 0;
        Playlist p = new Playlist();
        for (MusicLocation ml : locations) {
            if (ml.loc.getLatitude() < minLat) {
                minLat = ml.loc.getLatitude();
            }
            if (ml.loc.getLatitude() > maxLat) {
                maxLat = ml.loc.getLatitude();
            }
            if (ml.loc.getLongitude() < minLong) {
                minLong = ml.loc.getLongitude();
            }
            if (ml.loc.getLongitude() > maxLong) {
                maxLong = ml.loc.getLongitude();
            }

            p.merge(ml.playlist);
        }

        return new MusicLocation(midPoint(minLat, minLong, maxLat, maxLong), p);
    }

    private static Location midPoint(double lat1,double lon1,double lat2,double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //reconvert to degrees
        Location loc = new Location("");
        loc.setLatitude(Math.toDegrees(lat3));
        loc.setLongitude(Math.toDegrees(lon3));
        return loc;
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

    public Clusterer(List<MusicLocation> dataset, double eps) {
        _graph = new HashMap<Node, LinkedList<Node>>(dataset.size());
        List<Node> nodeset = new LinkedList<Node>();
        for (MusicLocation ml : dataset)
            nodeset.add(new Node(ml));

        for (int i = 0; i < nodeset.size(); ++i) {
            Node iNode = nodeset.get(i);
            for (int j = 0; j < i; ++j) {
                Node jNode = nodeset.get(j);
                double dist = iNode.val.loc.distanceTo(jNode.val.loc);
                if (dist < eps * 1000) {
                    if (_graph.get(iNode) == null) _graph.put(iNode, new LinkedList<Node>());
                    _graph.get(iNode).add(jNode);
                    if (_graph.get(jNode) == null) _graph.put(jNode, new LinkedList<Node>());
                    _graph.get(jNode).add(iNode);
                }
            }
        }
    }

    List<MusicLocation> DBSCAN(int minPts) {

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

        List<MusicLocation> merged = new LinkedList<MusicLocation>();
        for (List<MusicLocation> cl : clusters) {
            merged.add(mergeCluster(cl));
        }
        return merged;
    }

    void expandCluster(Node point, List<Node> neighbourhood, List<MusicLocation> cluster, int minPts) {
        cluster.add(point.val);
        point.clustered = true;
        ListIterator<Node> iter = neighbourhood.listIterator();
        while (iter.hasNext()) {
            Node n = iter.next();
            if (!n.visited) {
                n.visited = true;
                List<Node> nextNeighbourhood = _graph.get(n);
                if (nextNeighbourhood.size() >= minPts)
                    for (Node nextN : nextNeighbourhood) iter.add(nextN);
            }
            if (!n.clustered) {
                cluster.add(n.val);
                n.clustered = true;
            }
        }
    }

}
