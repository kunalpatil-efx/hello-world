package feature;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.intuit.karate.junit4.Karate;

import junit.framework.TestCase;

@RunWith(Karate.class)
public class KarateTest extends TestCase {
    
    @Test
    public void test1() {
        assertTrue(true);
    }
}