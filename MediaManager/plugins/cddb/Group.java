package plugins.cddb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
//import java.util.TreeMap;


/**
* Group is a resource made up by multiple individual resources; this could
* be an artist, a band, an orchestra, a chorus or anything similar -
* including yet another group. <p>
* Though normally a group would normally contain Artists rather than plain
* Resources, it is conceivable that a group also just contains
* Resource objects (let's say an orchestra or group is famous for using a
* specific instrument or something similar).
* @author Holger Antelmann
* @see Role
* @see Artist
* @see Resource
* @see Composition
*/
public class Group extends Resource
{
    HashSet resources = new HashSet();
    //TreeMap versions  = new TreeMap();


    public Group (String name, String description) {
        super(name, description);
    }


    /**
    * only looks at the directly added resources
    * (ignores matches in sub-groups)
    */
    public boolean contains (Resource resource) {
        return resources.contains(resource);
    }


    /** searches through sub-groups as well */
    public boolean find (Resource resource) {
        if (contains(resource)) return true;
        Iterator i = resources.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Group) {
                if (((Group)o).find(resource)) return true;
            }
        }
        return false;
    }


    /**
    * @throws RecursiveCycleException if the resource is a group that is already
    *                                 contained in a sub-level
    */
    public boolean add (Resource resource) throws RecursiveCycleException {
        if (resource instanceof Group) {
            if ((equals(resource)) || (find(resource)))
                throw new RecursiveCycleException(this);
        }
        return resources.add(resource);
    }


    public boolean remove (Resource resource) {
        return resources.remove(resource);
    }


    /**
    * provides an unmodifiable view of the embedded resources
    * @return Set of Resource objects
    */
    public Set getCompositionContributions () {
        return Collections.unmodifiableSet(resources);
    }
}
