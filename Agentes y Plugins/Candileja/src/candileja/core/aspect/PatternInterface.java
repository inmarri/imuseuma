/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package candileja.core.aspect;

/**
 *
 * @author Inma
 */
public interface PatternInterface {
    public boolean matchPattern(Object obj);
    public int hashCode();
    public boolean equals(Object obj);
}
