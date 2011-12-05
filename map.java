// The HashMap:

public class HashMap {
    private int size;
    private ArrayList<LinkedList<Element>> array;

    public HashMap() {
        this.size = 0;
        this.array = new ArrayList<LinkedList<Element>>(128);

        for (int i = 0; i < 128; i++) {
            this.array.add(new LinkedList<Element>());
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void add(String key, Object value) {
        this.remove(key);

        array.get(hash(key)).add(new Element(key, value));
        size++;
    }
    
    public boolean containsKey(String key) {
        for (Element ele : this.array.get(hash(key))) {
            if (ele.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return size;
    }

    public Object get(String key) {
        for (Element ele : this.array.get(hash(key))) {
            if (ele.key.equals(key)) {
                return ele.value;
            }
        }

        throw new NoSuchElementException();
    }
    
    private int hash(String key) {
        return key.hashCode() % 128;
    }

    public void remove(String key) {
        for (Element ele : this.array.get(hash(key))) {
            if (ele.key.equals(key)) {
                this.array.get(hash(key)).remove(ele);
                size--;
                break;
            }
        }
    }

    public boolean maybeContainsKey(String key) {
        return this.array.get(hash(key)).size() > 0;
    }

    private class Element {
        public final String key;
        public final Object value;
        
        public Element(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}

// The Tests:

public class HashMapTest {
    private HashMap map;
    
    @Before
    public void setUp() {
        this.map = new HashMap();
    }

    @Test
    public void testIsEmptyForNewMap() {
        assertTrue(map.isEmpty());
    } 
    
    @Test
    public void testAddMakesIsEmptyFalse() {
        map.add("Hello", 5);
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testSizeForNewMap() {
        assertEquals(0, map.size());
    }
    
    @Test
    public void testSizeIncrementsWhenAddingElements() {
        map.add("Hello", 5);
        assertEquals(1, map.size());
        
        map.add("Goodbye", 5);
        assertEquals(2, map.size());
    }
    
    @Test
    public void testGetReturnsCorrectValue() {
        map.add("Hello", 5);
        map.add("Goodbye", 6);
        assertEquals(5, map.get("Hello"));
        assertEquals(6, map.get("Goodbye"));
    }
    
    @Test(expected= NoSuchElementException.class)
    public void testThrowsExceptionIfKeyDoesNotExist() {
        map.get("Hello");
    }

    @Test
    public void testReplacesValueWithSameKey() {
        map.add("Hello", 5);
        map.add("Hello", 6);
        
        assertEquals(6, map.get("Hello"));
    }
    
    @Test
    public void testDoesNotOverwriteSeperateKeysWithSameHash() {
        map.add("Ea", 5);
        map.add("FB", 6);
        
        assertEquals(5, map.get("Ea"));
        assertEquals(6, map.get("FB"));
    }
    
    @Test
    public void testRemoveDoesNotEffectNewMap() {
        map.remove("Hello");

        assertEquals(0, map.size());
    }
    
    @Test
    public void testRemoveDecrementsSize() {
        map.add("Hello", 5);
        map.add("Goodbye", 6);
        
        map.remove("Hello");
        
        assertEquals(1, map.size());
        
        map.remove("Goodbye");
        
        assertEquals(0, map.size());
    }

    @Test(expected= NoSuchElementException.class)
    public void testRemoveDeletesElement() {
        map.add("Hello", 5);
        map.remove("Hello");
        
        map.get("Hello");
    }
    
    @Test
    public void testContainsKeyForNewMap() {
        assertFalse(map.containsKey("Hello"));
    }
    
    @Test
    public void testContainsKeyForNonExistingKey() {
        map.add("Hello", 5);
        assertFalse(map.containsKey("Goodbye"));
    }

    @Test
    public void testContainsKeyForExistingKey() {
        map.add("Hello", 5);
        assertTrue(map.containsKey("Hello"));
    }
    
    @Test
    public void testContainsKeyForKeyWithEquivalentHash() {
        map.add("Ea", 5);
        
        assertFalse(map.containsKey("FB"));
    }

    @Test
    public void testMaybeContainsKeyForNonNewMap() {
        assertFalse(map.maybeContainsKey("Hello"));
    }
    
    @Test
    public void testMaybeContainsKeyForKeyWithNoEquivalentHashInMap() {
        map.add("Hello", 5);

        assertFalse(map.maybeContainsKey("Goodbye"));
    }
    
    @Test
    public void testMaybeContainsKeyForKeyWithEquivalentHashInMap() {
        map.add("Ea", 5);
        
        assertTrue(map.maybeContainsKey("FB"));
    } 
}
