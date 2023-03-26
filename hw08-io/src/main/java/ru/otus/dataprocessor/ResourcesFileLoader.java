package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper = new ObjectMapper();
    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load()  throws FileProcessException {
        var inputStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        try{
        var list = (List<LinkedHashMap>)mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, LinkedHashMap.class));
        var result = new ArrayList<Measurement>(list.size());
        list.forEach(i-> result.add(new Measurement(i.get("name").toString(), Double.parseDouble(i.get("value").toString()))));
        return result;
        }catch (IOException e)
        {
            throw new FileProcessException(e);
        }
    }
}
