package io.tapdata.pdk.apis.spec;

import java.util.Map;

/**
 * Node specification to register node with form components.
 * When any one is creating a node, need to input information base on form components.
 * <p>
 * Node could be a Source, Target or Processor.
 */
public class TapNodeSpecification {
    private String name;
    private String id;
    private String group; //Unique key for each enterprise.
    private String version;
    private String icon;
    private Integer buildNumber;
    private Map<String, Object> applications;

    public String verify() {
        if(name == null)
            return "missing name";
        if(id == null)
            return "missing id";
        return null;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String idAndGroup() {
        return id + "@" + group;
    }

    public static String idAndGroup(String id, String group) {
        return id + "@" + group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Map<String, Object> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, Object> applications) {
        this.applications = applications;
    }
}
