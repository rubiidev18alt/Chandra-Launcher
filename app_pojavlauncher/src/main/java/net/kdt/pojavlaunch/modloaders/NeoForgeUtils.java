package net.kdt.pojavlaunch.modloaders;

import net.kdt.pojavlaunch.utils.DownloadUtils;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NeoForgeUtils {
    private static final String NEOFORGE_METADATA_URL = "https://maven.neoforged.net/releases/net/neoforged/neoforge/maven-metadata.xml";
    private static final String NEOFORGE_INSTALLER_URL = "https://maven.neoforged.net/releases/net/neoforged/neoforge/%1$s/neoforge-%1$s-installer.jar";
    private static final Pattern GAME_VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+).*");

    public static List<String> downloadNeoForgeVersions() throws IOException {
        SAXParser saxParser;
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            saxParser = parserFactory.newSAXParser();
        }catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return DownloadUtils.downloadStringCached(NEOFORGE_METADATA_URL, "neoforge_versions", input -> {
                try {
                    ForgeVersionListHandler handler = new ForgeVersionListHandler();
                    saxParser.parse(new InputSource(new StringReader(input)), handler);
                    return handler.getVersions();
                }catch (SAXException | IOException e) {
                    throw new DownloadUtils.ParseException(e);
                }
            });
        }catch (DownloadUtils.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getInstallerUrl(String version) {
        return String.format(NEOFORGE_INSTALLER_URL, version);
    }

    public static String getGameVersion(String neoForgeVersion) {
        Matcher matcher = GAME_VERSION_PATTERN.matcher(neoForgeVersion);
        if(!matcher.matches()) return neoForgeVersion;
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        return minor == 0 ? "1." + major : "1." + major + "." + minor;
    }
}
