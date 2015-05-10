/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.i18n;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.format.FormatProvider;
import org.tinygroup.format.Formater;
import org.tinygroup.format.PatternDefine;
import org.tinygroup.format.config.FormatPatternDefine;
import org.tinygroup.format.impl.DefaultPatternDefine;
import org.tinygroup.i18n.config.I18nConfiguration;
import org.tinygroup.i18n.config.I18nMessage;
import org.tinygroup.i18n.exception.I18nException;
import org.tinygroup.i18n.impl.I18nMessageContextImpl;

import java.util.*;

public final class I18nMessageFactory {
    private static Map<Locale, List<Properties>> customizeResourceMap = new HashMap<Locale, List<Properties>>();
    private static Map<Locale, List<Properties>> resourceMap = new HashMap<Locale, List<Properties>>();
    private static Locale defaultLocale = LocaleUtil.getContext().getLocale();
    private static I18nMessages i18nMessages = new I18nMessages();

    static {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        stream.alias("i18n-configuration", I18nConfiguration.class);
        stream.setClassLoader(I18nConfiguration.class.getClassLoader());
        I18nConfiguration configuration = (I18nConfiguration) stream
                .fromXML(I18nMessageFactory.class
                        .getResourceAsStream("/I18nConfiguration.xml"));
        List<I18nMessageStandard> i18nMessageStandards = new ArrayList<I18nMessageStandard>();
        for (I18nMessage msg : configuration.getI18nMessageStandards()) {
            I18nMessageStandard object;
            try {
                object = getObject(msg.getClassName());
                i18nMessageStandards.add(object);
            } catch (I18nException e) {
                throw new RuntimeException(e);
            }
        }
        List<I18nMessageContext> i18nMessageContexts = new ArrayList<I18nMessageContext>();
        try {
            Formater formatter = getObject(configuration.getFormater()
                    .getClassName());
            PatternDefine patternHandle = new DefaultPatternDefine();
            FormatPatternDefine formatPatternDefine = configuration
                    .getFormater().getFormatPatternDefine();
            patternHandle.setPrefixPatternString(formatPatternDefine
                    .getPrefixPatternString());
            patternHandle.setPatternString(formatPatternDefine
                    .getPatternString());
            patternHandle.setPostfixPatternString(formatPatternDefine
                    .getPostfixPatternString());
            formatter.setPatternHandle(patternHandle);

            Map<String, FormatProvider> formatProviders = new HashMap<String, FormatProvider>();
            for (org.tinygroup.format.config.FormatProvider formatProvider : configuration
                    .getFormater().getFormatProviders()) {
                FormatProvider fp = getObject(formatProvider.getClassName());
                formatProviders.put(formatProvider.getArea(), fp);
            }
            formatter.setFormatProviders(formatProviders);
            for (I18nMessage msg : configuration.getI18nMessageContexts()) {
                I18nMessageContextImpl object = getObject(msg.getClassName());
                i18nMessageContexts.add(object);
                object.setFormater(formatter);
            }
        } catch (I18nException e) {
            throw new RuntimeException(e);
        }
        i18nMessages.setI18nMessageStandards(i18nMessageStandards);
        i18nMessages.setI18nMessageContexts(i18nMessageContexts);
    }

    @SuppressWarnings("unchecked")
    private static <T> T getObject(String className) throws I18nException {
        try {
            Class<T> clazz;
            clazz = (Class<T>) Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new I18nException(e);
        }
    }

    private I18nMessageFactory() {

    }

    /**
     * 极少数的情况下，需要自己构建I18nMessages对象，此时可以由此注入
     *
     * @param i18nMessages
     */
    public static void setI18nMessages(I18nMessages i18nMessages) {
        I18nMessageFactory.i18nMessages = i18nMessages;
    }

    public static I18nMessages getI18nMessages() {
        return i18nMessages;
    }

    public static void cleanResource() {
        resourceMap.clear();
    }

    public static void addResource(Locale locale, Properties properties) {
        List<Properties> propertiesList = resourceMap.get(locale);
        if (propertiesList == null) {
            propertiesList = new ArrayList<Properties>();
            resourceMap.put(locale, propertiesList);
        }
        propertiesList.add(properties);
    }

    public static void removeResource(Locale locale, Properties properties) {
        List<Properties> propertiesList = resourceMap.get(locale);
        if (!CollectionUtil.isEmpty(propertiesList)) {
            propertiesList.remove(properties);
        }
    }

    public static void addCustomizeResource(Locale locale, Properties properties) {
        List<Properties> propertiesList = customizeResourceMap.get(locale);
        if (propertiesList == null) {
            propertiesList = new ArrayList<Properties>();
            customizeResourceMap.put(locale, propertiesList);
        }
        propertiesList.add(properties);
    }

    public static void removeCustomizeResource(Locale locale, Properties properties) {
        List<Properties> propertiesList = customizeResourceMap.get(locale);
        if (!CollectionUtil.isEmpty(propertiesList)) {
            propertiesList.remove(properties);
        }
    }


    public static String getMessage(String key) {
        return getMessage(LocaleUtil.getContext().getLocale(), key);
    }

    public static String getMessage(Locale locale, String key) {
        String message = getMessage(customizeResourceMap, locale, key);
        if (message != null) {
            return message;
        } else {
            return getMessage(resourceMap, locale, key);
        }
    }

    private static String getMessage(Map<Locale, List<Properties>> map, Locale locale, String key) {
        List<Properties> propertiesList = map.get(locale);
        if (propertiesList == null) {
            propertiesList = map.get(defaultLocale);
        }
        if (propertiesList != null) {
            for (Properties properties : propertiesList) {
                String value = properties.getProperty(key);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }
}
