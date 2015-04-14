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

    // Determine and store a value which can be used to compare (and hence sort)
    // addresses.  We attempt to extract numeric values from the Building Number,
    // Building Name, or Full Address.
    public void calculateSortingValue()
    {
        boolean success = false;
        if (!success)
        {
            success = getSortingValueFromBuildingNumber();
        }
        if (!success)
        {
            success = getSortingValueFromBuildingName();
        }
        if (!success)
        {
            success = getSortingValueFromAddressOfFlat();
        }
        if (!success)
        {
            sortingValue = 0;
        }
    }

    // Attempt to convert Building Number from string to numeric value.
    private boolean getSortingValueFromBuildingNumber()
    {
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

        return success;
    }

    // Attempt to convert Building Name from a string like '15A' into a numeric value.
    private boolean getSortingValueFromBuildingName()
    {
        return (buildingName != null) ? getSortingValueFromString(buildingName, 0, buildingName.length()) : false;
    }

    // Attempt to extract numeric value from addresses of the form 'Flat NNN, <etc>'.
    private boolean getSortingValueFromAddressOfFlat()
    {
        boolean success = false;

        if ((partial != null) && !partial.isEmpty() && partial.toLowerCase().startsWith("flat "))
        {
            // Determine where the Flat Number might end.
            int indexMax = partial.indexOf(',');
            if (indexMax == -1)
            {
                indexMax = partial.length();
            }
            
            // In the call below, we start searching the string from position #5
            // as this is the index of the first character after 'flat '.
            success = getSortingValueFromString(partial, 5, indexMax);
            
            // We want flats to be ordered after other address types (businesses
            // and houses), so we add a large offset.
            if (success)
            {
                sortingValue += 100000;
            }
        }
        
        return success;
    }
    
    // Helper function that attempts to extract a numeric value from a string, starting from
    // the specified position (inclusive) and stopping before the end position (exclusive).
    private boolean getSortingValueFromString(String source, int startIndex, int endIndex)
    {
        boolean success = false;

        if ((source != null) && !source.isEmpty() && (startIndex < endIndex) &&
            (endIndex <= source.length()) && Character.isDigit(source.charAt(startIndex)))
        {
            try
            {
                int index = startIndex;

                // First try to extract the numeric portion.
                sortingValue = 0;
                while ((index < endIndex) && Character.isDigit(source.charAt(index)))
                {
                    sortingValue = (sortingValue * 10) + Character.getNumericValue(source.charAt(index));
                    index++;
                }

                // Now, maybe there is a letter after the number, as in '15A'; try
                // to take account of this.  We're lazy and only take account of the
                // first letter.
                boolean foundFirstLetter = false;
                while ((index < endIndex) && !foundFirstLetter)
                {
                    if (Character.isLetter(source.charAt(index)))
                    {
                        foundFirstLetter = true;
                        sortingValue += (Character.getNumericValue(source.charAt(index)) / CHAR_MAX);
                    }
                    index++;
                }

                success = true;
            }
            catch (Exception e)
            {
                // We don't really care what the error was.
                success = false;
            }
        }

        return success;
    }

    // Allows addresses to be compared, in turn allowing them to be sorted.
    // We attempt to sort addresses such that business premises appear first,
    // then houses, then flats.  Within each group, we attempt to sort
    // alphabetically or numerically, as appropriate.
    public int compareTo(Address other)
    {
        int result = 0;

        if (((this.sortingValue != 0) || (other.sortingValue != 0)) &&
             (this.sortingValue != other.sortingValue))
        {
            // Use of signum is to get integer without losing precision.
            result = (int)Math.signum(this.sortingValue - other.sortingValue);
        }
        else if ((this.partial != null) && !this.partial.isEmpty() &&
                (other.partial != null) && !other.partial.isEmpty())
        {
            result = this.partial.compareToIgnoreCase(other.partial);
        }

        return result;
    }
}
