/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.hatemile.util;

import java.util.Random;
import org.hatemile.util.html.HTMLDOMElement;

/**
 * The IDGenerator class generate ids for
 * {@link org.hatemile.util.html.HTMLDOMElement}.
 */
public class IDGenerator {

    /**
     * The maximum length of random string.
     */
    public static final int MAXIMUM_RANDOM_STRING_LENGTH = 16;

    /**
     * The prefix of generated ids.
     */
    protected final String prefixId;

    /**
     * Count the number of ids created.
     */
    private int count;

    /**
     * Initializes a new object that generate ids for elements.
     */
    public IDGenerator() {
        prefixId = "id-hatemile-" + getRandom() + "-";
        count = 0;
    }

    /**
     * Initializes a new object that generate ids for elements.
     * @param prefixPart A part of prefix id.
     */
    public IDGenerator(final String prefixPart) {
        prefixId = "id-hatemile-" + prefixPart + "-" + getRandom() + "-";
        count = 0;
    }

    /**
     * Returns the random prefix.
     * @return The random prefix.
     */
    protected static String getRandom() {
        Random random = new Random();
        String randomString = Long.toHexString(random.nextLong());
        while (randomString.length() < MAXIMUM_RANDOM_STRING_LENGTH) {
            randomString += Long.toHexString(random.nextLong());
        }
        if (randomString.length() > MAXIMUM_RANDOM_STRING_LENGTH) {
            randomString = randomString.substring(0,
                    MAXIMUM_RANDOM_STRING_LENGTH - 1);
        }
        return randomString;
    }

    /**
     * Generate a id for a element.
     * @param element The element.
     */
    public void generateId(final HTMLDOMElement element) {
        if (!element.hasAttribute("id")) {
            element.setAttribute("id", prefixId + Integer.toString(count));
            count++;
        }
    }
}
