package net.nathem.script.enums;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.nathem.script.core.NathemObject;
import net.nathem.script.core.NathemWorld;
import net.nathem.script.core.Option;
import net.nathem.script.core.object.*;
import net.nathem.script.core.sign.Sign;

public enum ObjectType {
	
	EMPTY(null, null),
	CAPTOR("captor", Captor.class),
	REDSTONE("rs", Redstone.class),
	REDSTONE_SENSOR("rs sensor", RedstoneSensor.class),
	CUSTOM("custom", Custom.class),
	BLOCK("block", Block.class),
	MESSAGE("message", Message.class),
	TELEPORTER("tp", Teleporter.class),
	CLEAR_INVENTORY("ci", ClearInventory.class),
	SPAWNER("spawner", Spawner.class),
	STRIKE("strike", Strike.class),
	BUTCHER("butcher", Butcher.class),
	STUFF("stuff", Stuff.class),
	MARKER("marker", Marker.class), 
	GIVE("give", Give.class),
	CHEST("chest", Chest.class),
	KILL("kill", Kill.class),
	HEAL("heal", Heal.class),
	FEED("feed", Feed.class),
	EXPLOSION("explosion", Explosion.class),
	RANDOM("random", Random.class),
	TAG("tag", Tag.class),
    CHECKPOINT("checkpoint", CheckPoint.class),
    END("end",End.class);
	
	private String objectName;
	private Class<? extends NathemObject> objectClass;
	
	ObjectType(String objectName, Class<? extends NathemObject> objectClass)
	{
		this.objectName = objectName;
		this.objectClass = objectClass;
	}

	public String getObjectName() {
		return objectName;
	}

	public Class<? extends NathemObject> getObjectClass() {
		return objectClass;
	}
	
	public NathemObject createNew(Sign sign, NathemWorld nathemWorld)
	{
		try {
			return (NathemObject) this.objectClass.getDeclaredConstructor(Sign.class, NathemWorld.class).newInstance(sign, nathemWorld);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public NathemObject createNew()
	{
		try {
			return (NathemObject) this.objectClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
		
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String, Option> getOptionsList()
	{
			NathemObject bo = this.createNew();
			return bo.getOptionsList();
	}
	
	private static final Map<String, ObjectType> OBJECT_NAME_MAP = new HashMap<String, ObjectType>();
	
	static
	{
		for(ObjectType objectType : values())
		{
			OBJECT_NAME_MAP.put(objectType.getObjectName(), objectType);
		}
	}
	
	public static ObjectType fromObjectName(String objectName)
	{
		return OBJECT_NAME_MAP.get(objectName);
	}
	
	public static ArrayList<String> getObjectTypeNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		for(ObjectType ot : ObjectType.OBJECT_NAME_MAP.values())
		{
			if(ot.getObjectName() != null)
			{
				names.add(ot.getObjectName());
			}
		}
		return names;
	}
	
	
}
