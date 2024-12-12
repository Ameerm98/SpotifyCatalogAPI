package com.example.catalog;

import com.example.catalog.utils.LRUCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    LRUCache<String, String> cache;

    @Nested
    @DisplayName("when instantiated with capacity 3")
    class WhenInstantiated {

        @BeforeEach
        void createNewCache() {
            cache = new LRUCache<>(3);
        }

        @Test
        @DisplayName("cache is initially empty")
        void isEmpty() {
            // TODO assert cache is empty
            assertTrue(cache.isEmpty());
        }

        @Test
        @DisplayName("throws NullPointerException when getting a null key")
        void throwsExceptionWhenGettingNullKey() {
            // TODO assert NullPointerException thrown on `cache.get(null)`
            assertThrows(NullPointerException.class,()->{
                cache.get(null);
            });
            assertThrows(NullPointerException.class,()->{
                cache.set(null,null);
            });
        }

        @Nested
        @DisplayName("after adding 2 elements")
        class AfterAdding2Elements {

            @BeforeEach
            void addElements() {
                // TODO add 2 elements
                cache.set("Mahmoud","Masarwa");
                cache.set("Malik","Atamnh");
            }

            @Test
            @DisplayName("cache contains the added elements")
            void containsAddedElements() {
                // TODO assert the added 2 elements are available
                assertSame("Masarwa", cache.get("Mahmoud"));
                assertSame("Atamnh", cache.get("Malik"));
            }

            @Test
            @DisplayName("cache maintains the same size and content after adding duplicate keys")
            void noDuplicates(){
                cache.set("Mahmoud","Masarwa");
                cache.set("Malik","Atamnh");
                assertEquals(2, cache.size());
                assertSame("Masarwa", cache.get("Mahmoud"));
                assertSame("Atamnh", cache.get("Malik"));
            }
            @Test
            @DisplayName("ablity to add one more element without revoke another existed keys")
            void abilityToAddMoreElement(){
                cache.set("Aws","Masarwa");
                assertEquals(3, cache.size());
            }
            @Test
            @DisplayName("maintains correct order of elements after accessing an existing element")
            void correctOrderOfElements(){
                //Map.Entry<String,String> First = cache.firstEntry();
                //assertEquals("Malik",First.getKey());
                //assertEquals("Atamnh",First.getValue());
                }
            @Test
            @DisplayName("updates the value of an existing key when set again")
            void updateExistingKeyValue(){
                cache.set("Mahmoud","Msa");
                assertEquals("Msa",cache.get("Mahmoud"));

            }
        }

        @Nested
        @DisplayName("after adding 3 elements")
        class AfterAdding3Elements {
            @BeforeEach
            void addElements() {
                // TODO add 3 elements
                cache.set("Mahmoud","Masarwa");
                cache.set("Malik","Atamnh");
                cache.set("Aws","Masarwa");
            }
            @Test
            @DisplayName("size does not exceed capacity after adding more elements")
            void sizeDoesntExceedCapacity(){
                cache.set("Fade","Masarwa");
                assertEquals(3,cache.size());
            }
            @Test
            @DisplayName("evicts the least recently used element when the cache exceeds its capacity")
            void evictsLeastUsedElement(){
                cache.get("Mahmoud");
                cache.get("Malik");
                cache.set("Karam","Atamnh");
                assertThrows(NullPointerException.class,()->{cache.get("Fade");});
            }



            @Nested
            @DisplayName("when cleared")
            class WhenCleared {
                // addElements (in AfterAdding3Elements) is executed and then clearCache
                // before EACH test case in WhenCleared

                @Test
                @DisplayName("cache is empty")
                void isEmpty() {
                    // TODO assert cache is empty
                    assertTrue(cache.isEmpty());
                }
                @Test
                @DisplayName("added elements are not accesible")
                void noAccesibleElements(){
                    // TODO assert cache is empty
                    assertThrows(NullPointerException.class,()->{cache.get("Fade");});
                    assertThrows(NullPointerException.class,()->{cache.get("Ameer");});
                    assertThrows(NullPointerException.class,()->{cache.get("Malik");});
                }
                @BeforeEach
                void clearCache() {
                    // TODO clear the cache after
                    cache.clear();
                }
            }
        }
        @Nested
        @DisplayName("when working with boundary conditions:")
        class WorkingWithBoundaryConditions{
            @Test
            @DisplayName("handles adding elements up to exactly the capacity without eviction")
            void fillCache(){
                cache.set("Mahmoud","Masarwa");
                cache.set("Malik","Atamnh");
                cache.set("Aws","Masarwa");
            }

            @Test
            @DisplayName("evicts only the least recently used element when capacity is exceeded repeatedly")
            void evictsOnlyLeastRecentlyUsedElement(){
                cache.get("Mahmoud");
                cache.get("Malik");
                cache.set("Karam","Atamna");
                assertThrows(NullPointerException.class,()->{cache.get("Aws");});
            }
        }
    }
}
