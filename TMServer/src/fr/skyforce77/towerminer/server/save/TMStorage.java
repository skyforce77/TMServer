package fr.skyforce77.towerminer.server.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TMStorage implements Serializable {

	private static final long serialVersionUID = -7775936155165721980L;
	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private HashMap<String, Object> objects = new HashMap<String, Object>();
	private HashMap<String, Integer> integers = new HashMap<String, Integer>();
	private HashMap<String, Long> longs = new HashMap<String, Long>();
	private HashMap<String, Double> doubles = new HashMap<String, Double>();
	private HashMap<String, Float> floats = new HashMap<String, Float>();
	private HashMap<String, Byte> bytes = new HashMap<String, Byte>();
	private HashMap<String, TMStorage> storages = new HashMap<String, TMStorage>();

	public TMStorage(){};
	
	public void addObject(String key, Object value)
	{
		objects.put(key,value);
	}
	
	public void addString(String key, String value)
	{
		strings.put(key,value);
	}
	
	public void addInteger(String key, Integer value)
	{
		integers.put(key,value);
	}
	
	public void addLong(String key, Long value)
	{
		longs.put(key,value);
	}
	
	public void addDouble(String key, Double value)
	{
		doubles.put(key,value);
	}
	
	public void addFloat(String key, Float value)
	{
		floats.put(key,value);
	}
	
	public void addByte(String key, Byte value)
	{
		bytes.put(key,value);
	}
	
	public void addSWStorage(String key, TMStorage value)
	{
		storages.put(key,value);
	}
	
	public Object getObject(String key)
	{
		return objects.get(key);
	}
	
	public String getString(String key)
	{
		if(strings.containsKey(key))
		{
			return strings.get(key);
		}
		else
		{
			return "";
		}
	}
	
	public Integer getInteger(String key)
	{
		if(integers.containsKey(key))
		{
			return integers.get(key);
		}
		else
		{
			return 0;
		}
	}
	
	public Long getLong(String key)
	{
		if(longs.containsKey(key))
		{
			return longs.get(key);
		}
		else
		{
			return (long)0;
		}
	}
	
	public Double getDouble(String key)
	{
		if(doubles.containsKey(key))
		{
			return doubles.get(key);
		}
		else
		{
			return (double)0;
		}
	}
	
	public Float getFloat(String key)
	{
		if(floats.containsKey(key))
		{
			return floats.get(key);
		}
		else
		{
			return (float)0;
		}
	}
	
	public Byte getByte(String key)
	{
		if(bytes.containsKey(key))
		{
			return bytes.get(key);
		}
		else
		{
			return (byte)0;
		}
	}
	
	public TMStorage getTMStorage(String key)
	{
		if(storages.containsKey(key))
		{
			return storages.get(key);
		}
		else
		{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList<HashMap> getValues()
	{
		ArrayList<HashMap> maps = new ArrayList<HashMap>();
		maps.add(this.bytes);
		maps.add(this.doubles);
		maps.add(this.floats);
		maps.add(this.integers);
		maps.add(this.longs);
		maps.add(this.objects);
		maps.add(this.storages);
		maps.add(this.strings);
		return maps;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void add(TMStorage storage)
	{
		ArrayList<HashMap> maps = storage.getValues();
		this.bytes.putAll(maps.get(0));
		this.doubles.putAll(maps.get(1));
		this.floats.putAll(maps.get(2));
		this.longs.putAll(maps.get(4));
		this.objects.putAll(maps.get(5));
		this.storages.putAll(maps.get(6));
		this.strings.putAll(maps.get(7));
		
		for(Object s : maps.get(3).keySet())
		{
			if(!((String)s).equals("StorageType"))
			{
				integers.put((String)s, (Integer)maps.get(3).get(s));
			}
		}
	}
	
	public Boolean Serialize(String file)
	{
		try
		{
			new File(file).getParentFile().mkdirs();
			FileOutputStream fichier = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			return true;
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static TMStorage Deserialize(String file)
	{
		try
		{
			FileInputStream fichier = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			TMStorage o = (TMStorage)ois.readObject();
			ois.close();
			return o;
		}
		catch (Exception e) 
		{
			return new TMStorage();
		}
	}

}
