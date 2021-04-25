package model;

import java.util.Map;

public interface RowCompatible
{
    Map<String,String> toRow();
    String getIndexName();
}
