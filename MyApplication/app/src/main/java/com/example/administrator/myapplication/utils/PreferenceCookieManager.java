package com.example.administrator.myapplication.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;

/**
 * Created by Administrator on 2016/8/5.
 */
public class PreferenceCookieManager {

    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String NAME_SPLITER = ",";
    private final ConcurrentHashMap<String, Cookie> cookies;
    private final SharedPreferences cookiePrefs;

    public PreferenceCookieManager(Context context) {
        this.cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        this.cookies = new ConcurrentHashMap<>();

        String storedCookieNames = this.cookiePrefs.getString(COOKIE_NAME_STORE, (String) null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, NAME_SPLITER);
            String[] var7 = cookieNames;
            int var6 = cookieNames.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                String name = var7[var5];
                String encodedCookie = this.cookiePrefs.getString(COOKIE_NAME_PREFIX + name, (String) null);
                if (encodedCookie != null) {
                    Cookie decodeCookie = this.decodeCookie(encodedCookie);
                    if (decodeCookie != null) {
                        this.cookies.put(name, decodeCookie);
                    }
                }
            }
            this.clearExpired(new Date());
        }
    }

    public void addCookie(Cookie cookie) {
        String name = cookie.name();
        if (cookie.persistent()) {
            this.cookies.put(name, cookie);
        } else {
            this.cookies.remove(name);
        }

        SharedPreferences.Editor editor = this.cookiePrefs.edit();
        editor.putString("names", TextUtils.join(",", this.cookies.keySet()));
        editor.putString("cookie_" + name, this.encodeCookie(new PreferenceCookieManager.SerializableCookie(cookie)));
        editor.commit();
    }

    public void clear() {
        SharedPreferences.Editor editor = this.cookiePrefs.edit();
        Iterator var3 = this.cookies.keySet().iterator();

        while (var3.hasNext()) {
            String name = (String) var3.next();
            editor.remove(COOKIE_NAME_PREFIX + name);
        }

        editor.remove(COOKIE_NAME_STORE);
        editor.commit();
        this.cookies.clear();
    }

    public List<Cookie> getCookies() {
        return new ArrayList(this.cookies.values());
    }

    public Cookie getCookie(String name) {
        return (Cookie) this.cookies.get(name);
    }

    private Cookie decodeCookie(String cookieStr) {
        byte[] bytes = this.hexStringToByteArray(cookieStr);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream e = new ObjectInputStream(is);
            cookie = ((PreferenceCookieManager.SerializableCookie) e.readObject()).getCookies();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    protected String encodeCookie(PreferenceCookieManager.SerializableCookie cookie) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ObjectOutputStream e = new ObjectOutputStream(os);
            e.writeObject(cookie);
        } catch (Throwable var4) {
            return null;
        }

        return this.byteArrayToHexString(os.toByteArray());
    }

    public boolean clearExpired(Date date) {
        boolean clearedAny = false;
        SharedPreferences.Editor editor = this.cookiePrefs.edit();
        Iterator var5 = this.cookies.entrySet().iterator();

        while (true) {
            String name;
            Cookie cookie;
            do {
                if (!var5.hasNext()) {
                    if (clearedAny) {
                        editor.putString("names", TextUtils.join(NAME_SPLITER, this.cookies.keySet()));
                    }

                    editor.commit();
                    return clearedAny;
                }
                Map.Entry entry = (Map.Entry) var5.next();
                name = (String) entry.getKey();
                cookie = (Cookie) entry.getValue();

            } while (cookie.persistent());

            this.cookies.remove(name);
            editor.remove(COOKIE_NAME_PREFIX + name);
            clearedAny = true;
        }
    }

    protected String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        byte[] var6 = b;
        int var5 = b.length;

        for (int var4 = 0; var4 < var5; ++var4) {
            byte element = var6[var4];
            int v = element & 255;
            if (v < 16) {
                sb.append('0');
            }

            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase();
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public class SerializableCookie implements Serializable {

        private transient final Cookie cookies;
        private transient Cookie clientCookies;

        public SerializableCookie(Cookie cookies) {
            this.cookies = cookies;
        }

        public Cookie getCookies() {
            Cookie bestCookies = cookies;
            if (clientCookies != null) {
                bestCookies = clientCookies;
            }
            return bestCookies;
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeObject(cookies.name());
            out.writeObject(cookies.value());
            out.writeLong(cookies.expiresAt());
            out.writeObject(cookies.domain());
            out.writeObject(cookies.path());
            out.writeBoolean(cookies.secure());
            out.writeBoolean(cookies.httpOnly());
            out.writeBoolean(cookies.hostOnly());
            out.writeBoolean(cookies.persistent());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            String name = (String) in.readObject();
            String value = (String) in.readObject();
            long expiresAt = in.readLong();
            String domain = (String) in.readObject();
            String path = (String) in.readObject();
            boolean secure = in.readBoolean();
            boolean httpOnly = in.readBoolean();
            boolean hostOnly = in.readBoolean();
            boolean persistent = in.readBoolean();
            Cookie.Builder builder = new Cookie.Builder();
            builder = builder.name(name);
            builder = builder.value(value);
            builder = builder.expiresAt(expiresAt);
            builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
            builder = builder.path(path);
            builder = secure ? builder.secure() : builder;
            builder = httpOnly ? builder.httpOnly() : builder;
            clientCookies = builder.build();
        }
    }

}
