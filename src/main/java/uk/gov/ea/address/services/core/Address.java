package uk.gov.ea.address.services.core;

import java.util.List;
import java.lang.Comparable;

public class Address implements Comparable<Address>
{
    private String moniker;

    private String uprn;

    private List<String> lines;

    private String town;

    private String postcode;

    private String easting;

    private String northing;

    private String country;

    private String dependentLocality;

    private String dependentThroughfare;

    private String administrativeArea;

    private String localAuthorityUpdateDate;

    private String royalMailUpdateDate;

    private String partial;

    private String subBuildingName;

    private String buildingName;

    private String thoroughfareName;

    private String organisationName;

    private String buildingNumber;

    private String postOfficeBoxNumber;

    private String departmentName;

    private String doubleDependentLocality;

    // Not part of the address; used only for comparing this address to another
    // in order to sort several addresses into a pleasant order.
    private double sortingValue;
    private static final double CHAR_MAX = 65536.0;

    public String getUprn()
    {
        return uprn;
    }

    public void setUprn(String uprn)
    {
        this.uprn = uprn;
    }

    public List<String> getLines()
    {
        return lines;
    }

    public void setLines(List<String> lines)
    {
        this.lines = lines;
    }

    public String getPostcode()
    {
        return postcode;
    }

    public void setPostcode(String postcode)
    {
        this.postcode = postcode;
    }

    public String getMoniker()
    {
        return moniker;
    }

    public void setMoniker(String moniker)
    {
        this.moniker = moniker;
    }

    public String getPartial()
    {
        return partial;
    }

    public void setPartial(String partial)
    {
        this.partial = partial;
    }

    public String getTown()
    {
        return town;
    }

    public void setTown(String town)
    {
        this.town = town;
    }

    public String getEasting()
    {
        return easting;
    }

    public void setEasting(String eastings)
    {
        this.easting = eastings;
    }

    public String getNorthing()
    {
        return northing;
    }

    public void setNorthing(String northings)
    {
        this.northing = northings;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getDependentLocality()
    {
        return dependentLocality;
    }

    public void setDependentLocality(String dependentLocality)
    {
        this.dependentLocality = dependentLocality;
    }

    public String getDependentThroughfare()
    {
        return dependentThroughfare;
    }

    public void setDependentThroughfare(String dependentThroughfare)
    {
        this.dependentThroughfare = dependentThroughfare;
    }

    public String getAdministrativeArea()
    {
        return administrativeArea;
    }

    public void setAdministrativeArea(String administrativeArea)
    {
        this.administrativeArea = administrativeArea;
    }

    public String getLocalAuthorityUpdateDate()
    {
        return localAuthorityUpdateDate;
    }

    public void setLocalAuthorityUpdateDate(String localAuthorityUpdateDate)
    {
        this.localAuthorityUpdateDate = localAuthorityUpdateDate;
    }

    public String getRoyalMailUpdateDate()
    {
        return royalMailUpdateDate;
    }

    public void setRoyalMailUpdateDate(String royalMailUpdateDate)
    {
        this.royalMailUpdateDate = royalMailUpdateDate;
    }

    public String getSubBuildingName()
    {
        return subBuildingName;
    }

    public void setSubBuildingName(String subBuildingName)
    {
        this.subBuildingName = subBuildingName;
    }

    public String getBuildingName()
    {
        return buildingName;
    }

    public void setBuildingName(String buildingName)
    {
        this.buildingName = buildingName;
    }

    public String getThoroughfareName()
    {
        return thoroughfareName;
    }

    public void setThoroughfareName(String thoroughfareName)
    {
        this.thoroughfareName = thoroughfareName;
    }

    public String getOrganisationName()
    {
        return organisationName;
    }

    public void setOrganisationName(String organisationName)
    {
        this.organisationName = organisationName;
    }

    public String getBuildingNumber()
    {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber)
    {
        this.buildingNumber = buildingNumber;
    }

    public String getPostOfficeBoxNumber()
    {
        return postOfficeBoxNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber)
    {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDoubleDependentLocality()
    {
        return doubleDependentLocality;
    }

    public void setDoubleDependentLocality(String doubleDependentLocality)
    {
        this.doubleDependentLocality = doubleDependentLocality;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (uprn != null)
        {
            sb.append("[uprn=" + uprn + "]");
        }
        if (partial != null)
        {
            sb.append(partial);
        }
        else if (lines != null)
        {
            for (String line : lines)
            {
                sb.append(line + ", ");
            }
            if (town != null)
            {
                sb.append(town + ", ");
            }
            sb.append(postcode);
        }
        return sb.toString();
    }
    
    // Determines a value, based upon the building number, which can be used
    // to sort addresses into a user-friendly order.  We attempt to take account
    // of flat numbers such as '15A' in addition to just the raw number.
    public void calculateSortingValue()
    {
        sortingValue = 0;
        
        // First, attempt to convert Building Number into a numeric value.
        boolean success = false;
        if ((buildingNumber != null) && !buildingNumber.isEmpty())
        {
            try
            {
                sortingValue = Integer.parseInt(buildingNumber);
                success = true;
            }
            catch (NumberFormatException e)
            {
                success = false;
            }
        }
        
        // If that didn't work, attempt to extract a number from the Building Name.
        try
        {
            if (!success && (buildingName != null) && !buildingName.isEmpty() && Character.isDigit(buildingName.charAt(0)))
            {
                int len = buildingName.length();
                int index = 0;
                
                // First try to extract the numeric portion.
                while ((index < len) && Character.isDigit(buildingName.charAt(index)))
                {
                    sortingValue = (sortingValue * 10) + Character.getNumericValue(buildingName.charAt(index));
                    index++;
                }
                
                // Now, maybe there is a letter after the number, as in '15A'; try
                // to take account of this.  We're lazy and only take account of the
                // first letter.
                boolean foundFirstLetter = false;
                while ((index < len) && !foundFirstLetter)
                {
                    if (Character.isLetter(buildingName.charAt(index)))
                    {
                        foundFirstLetter = true;
                        sortingValue += (Character.getNumericValue(buildingName.charAt(index)) / CHAR_MAX);
                    }
                    index++;
                }
            }
        }
        catch (Exception e)
        {
            // We don't really care what the error was; just swallow it and
            // allow the address to be sorted via its text value.
            sortingValue = 0;
        }
    }
    
    // Allows addresses to be compared, in turn allowing them to be sorted.
    public int compareTo(Address other)
    {
        int result = 0;
        
        if ((this.sortingValue != 0) || (other.sortingValue != 0))
        {
            // The multiplication here is required because we need to return
            // an integer, but our sorting values may contain fractional values.
            result = (int)((this.sortingValue - other.sortingValue) * CHAR_MAX);
        }
        else if ((this.partial != null) && !this.partial.isEmpty() &&
                (other.partial != null) && !other.partial.isEmpty())
        {
            result = this.partial.compareToIgnoreCase(other.partial);
        }
        
        return result;
    }
}
