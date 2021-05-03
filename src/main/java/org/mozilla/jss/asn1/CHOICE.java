/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.jss.asn1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Objects of this class are generated by CHOICE.Template.decode(). It is
 * not necessary to use them to encode a CHOICE. Since the encoding of a
 * CHOICE is simply the encoding of the chosen element, it is easier
 * to just write out the chosen element.
 */
public class CHOICE implements ASN1Value {

    private Tag tag;
    private ASN1Value val;

    /**
     * Create a CHOICE whose chosen element has an implicit tag.
     * @param implicitTag Implicit tag.
     * @param val The value.
     */
    public CHOICE(Tag implicitTag, ASN1Value val) {
        tag = implicitTag;
        this.val = val;
    }

    /**
     * Create a CHOICE whose chosen element has no implicit tag.
     * @param val The value.
     */
    public CHOICE(ASN1Value val) {
        this.tag = val.getTag();
        this.val = val;
    }

    /**
     * Returns the tag that the chosen element is encoded with, which is
     * either the underlying tag of the element or an implicit tag.
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * @return The chosen value.
     */
    public ASN1Value getValue() {
        return val;
    }

    public static CHOICE.Template getTemplate() {
        return new CHOICE.Template();
    }

    /**
     * Encodes this CHOICE.  This merely consists of encoding the chosen
     * element with an implicit tag, if one was given in the constructor,
     * or with its own underlying tag.
     */
    public void encode( OutputStream ostream ) throws IOException {
        val.encode( tag, ostream );
    }

    /**
     * Encodes this CHOICE.  This merely consists of encoding the chosen
     * element with an implicit tag, if one was given in the constructor,
     * or with its own underlying tag.
     *
     * @param implicitTag <b>This value is ignored</b>. The tag of a CHOICE
     *      is merely the tag of the chosen element of the CHOICE.  A
     *      CHOICE cannot itself have an implicit tag.
     */
    public void encode( Tag implicitTag, OutputStream ostream )
        throws IOException
    {
        assert(implicitTag.equals(tag));
        val.encode( tag, ostream );
    }

/**
 * A Template for decoding ASN.1 <code>CHOICE</code>s
 */
public static class Template implements ASN1Template {

    // The the various possibilities in this CHOICE
    private Vector<Element> templates = new Vector<>();

    /**
     *  Creates an empty CHOICE template
     */
    public Template() { }

    /**
     * Adds a new sub-template to this CHOICE template with no implicit tag.
     * @param template Sub-template.
     */
    public void addElement( ASN1Template template ) {
        templates.addElement( new Element( template ) );
    }

    /**
     * Adds a new sub-template to this CHOICE template with an implicit tag.
     * @param implicitTag Implicit tag.
     * @param template Sub-template.
     */
    public void addElement( Tag implicitTag, ASN1Template template) {
        templates.addElement( new Element( implicitTag, template) );
    }

    /**
     * @return The number of elements in this CHOICE template.
     */
    public int size() {
        return templates.size();
    }

    /**
     * @param index Element index.
     * @return The element at the specified index.
     */
    public ASN1Template elementAt(int index) {
        return templates.elementAt(index).getTemplate();
    }

    /**
     * Retrieves the implicit tag of the element at the specified index.
     * Returns null if there is no implicit tag for this element.
     * @param index Element index.
     * @return The implicit tag.
     */
    public Tag implicitTagAt(int index) {
        return templates.elementAt(index).getImplicitTag();
    }

    /**
     * Empties this CHOICE template.
     */
    public void removeAllElements() {
        templates.removeAllElements();
    }

    /**
     * Removes the element at the specified index.
     * @param index Element index.
     */
    public void removeElementAt(int index) {
        templates.removeElementAt(index);
    }

    /**
     * Determines whether the given tag will satisfy this template.
     * @param t The tag.
     * @return True if the tag satisfies any sub-template.
     */
    public boolean tagMatch(Tag t) {
        int size = size();
        for(int i = 0; i < size; i++) {
            Tag impl = implicitTagAt(i);
            if( impl != null ) {
                // There is an implicit tag, if we match it we have a match
                if( impl.equals(t) ) {
                    return true;
                }
            } else {
                // no implicit tag, look at the sub-template itself
                ASN1Template templ = elementAt(i);
                if( templ.tagMatch(t) ) {
                    return true;
                }
            }
        }

        // none of the elements matched
        return false;
    }

    public ASN1Value decode(InputStream istream)
        throws IOException, InvalidBERException
    {
        ASN1Header head = ASN1Header.lookAhead(istream);
        Tag tag = head.getTag();

        // Loop over all the elements of the CHOICE template until we
        // find one with a matching tag.
        int size = size();
        for(int i=0; i < size; i++) {
            if( implicitTagAt(i) != null ) {
                if( implicitTagAt(i).equals(tag) ) {
                    // match by implicit tag!
                    ASN1Value val = elementAt(i).decode( implicitTagAt(i),
                                                           istream );
                    //return elementAt(i).decode( implicitTagAt(i), istream );
                    return new CHOICE( implicitTagAt(i), val );
                }
            } else {
                if( elementAt(i).tagMatch(tag) ) {
                    // match by base tag !
                    //return elementAt(i).decode(istream);
                    return new CHOICE( elementAt(i).decode(istream) );
                }
            }
        }

        // we didn't find any match
        throw new InvalidBERException("Unable to decode CHOICE");
    }

    // Implicit tags are illegal for CHOICE (and ANY)
    /**
     * Decodes a CHOICE.
     * @param implicitTag <b>This parameter is ignored.</b> A choice
     *      cannot have an implicit tag.
     */
    public ASN1Value decode(Tag implicitTag, InputStream istream)
        throws IOException, InvalidBERException
    {
        return decode(istream);
    }

    /**
     * An element in a CHOICE template, consisting of a nested template
     *  and, optionally, an implicit tag for that template.
     */
    private static class Element {
        private ASN1Template template;
        private Tag implicitTag=null;

        /**
         * Creates a CHOICE template element with no implicit tag.
         */
        public Element(ASN1Template template) {
            this.template = template;
        }

        /**
         * Creates a CHOICE template element with an implicit tag.
         */
        public Element(Tag implicitTag, ASN1Template template) {
            this.template = template;
            this.implicitTag = implicitTag;
        }

        /**
         * Returns the template of this CHOICE template element.
         */
        public ASN1Template getTemplate() {
            return template;
        }

        /**
         * Returns the implicit tag for this CHOICE template element,
         *  if there is one.  If not, returns null.
         */
        public Tag getImplicitTag() {
            return implicitTag;
        }
    }
}

}
