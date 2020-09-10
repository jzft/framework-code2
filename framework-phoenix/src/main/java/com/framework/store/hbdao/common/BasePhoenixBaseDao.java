package com.framework.store.hbdao.common;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.framework.store.hbdao.annontation.HBaseColumn;
import com.framework.store.hbdao.annontation.HBaseEntity;
import com.framework.store.hbdao.annontation.Transient;
import com.framework.store.hbdao.common.entity.BaseHBaseEntity;
import com.framework.store.hbdao.common.entity.IBaseEntity;
import com.framework.utils.ClassUtils;
import com.framework.utils.DateUtil;
import com.framework.utils.NumUtil;
//import com.neusoft.store.hbdao.entity.single.SinglePlaneInfoEntity;
//import com.neusoft.utils.DateUtils;
//import com.neusoft.utils.NumberUtil;

@Service
public class BasePhoenixBaseDao implements IBaseDao {
	
	
	
	
	Map<Class<?>, Map<String, FieldMeta>> hbaseMata = new HashMap<Class<?>, Map<String, FieldMeta>>();
	protected static Logger logger = Logger.getLogger(BasePhoenixBaseDao.class);
	@Autowired
	private JdbcTemplate phoenixJdbcTemplete = null;
	
	public <T extends IBaseEntity> void createTable(Class<T> clazz) {
		createTable_new(clazz);
		// String tableName = this.getHBaseTableName(clazz);
		// String sql = "create table if not exists %s (rowkey VARCHAR primary
		// key %s)";
		// String colSql = getTableColSql(clazz);
		// System.out.println(colSql);
		// sql = String.format(sql, tableName, StringUtils.isEmpty(colSql) ? ""
		// : ("," + colSql));
		// logger.debug(sql);
		// System.out.println(sql);
		// phoenixJdbcTemplete.execute(sql);

		// this.createTableCol(clazz);
	}
	
	private <T extends IBaseEntity> void createTable_new(Class<T> clazz) {
		String saltBuckets = null;
		String timeToLive = null;
		String tableName = StringUtils.upperCase(this.getHBaseTableName(clazz));
		try {
			BaseHBaseEntity t = (BaseHBaseEntity) clazz.newInstance();
			saltBuckets = t.setSaltBuckets();
			timeToLive = t.setTimeToLive();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String colSql =StringUtils.lowerCase(getTableColSqlWithOneMany(clazz));
		String sql = "";
		if(!StringUtils.isEmpty(timeToLive)){
			if(!StringUtils.isEmpty(saltBuckets) && !"0".equals(saltBuckets)){
				sql = "create table if not exists %s (\"id\" VARCHAR NOT NULL, TSCREATEDDATE DATE NOT NULL %s CONSTRAINT PK PRIMARY KEY (ROWKEY,TSCREATEDDATE ROW_TIMESTAMP))TTL=%s,SALT_BUCKETS=%s";
				sql = String.format(sql, tableName, StringUtils.isEmpty(colSql) ? "" : (","+colSql), timeToLive, saltBuckets);
			}
			else{
				sql = "create table if not exists %s (\"id\" VARCHAR NOT NULL, TSCREATEDDATE DATE NOT NULL %s CONSTRAINT PK PRIMARY KEY (ROWKEY,TSCREATEDDATE ROW_TIMESTAMP))TTL=%s";
				sql = String.format(sql, tableName, StringUtils.isEmpty(colSql) ? "" : (","+colSql), timeToLive);
			}
		}
		else{
			if(!StringUtils.isEmpty(saltBuckets) && !"0".equals(saltBuckets)){
				sql = "create table if not exists %s (\"id\" VARCHAR primary key %s) SALT_BUCKETS=%s";
				sql = String.format(sql, tableName, StringUtils.isEmpty(colSql) ? "" : ("," + colSql),saltBuckets);
			}
			else{
				sql = "create table if not exists %s (\"id\" VARCHAR primary key %s)";
				sql = String.format(sql, tableName, StringUtils.isEmpty(colSql) ? "" : ("," + colSql));
			}
		}
		System.out.println(sql);
		phoenixJdbcTemplete.execute(sql);
		// this.createTableCol(clazz);
	}

	@Override
	public <T extends IBaseEntity> T get(Class<T> clazz, String id) {
		String tableName = this.getHBaseTableName(clazz);
		String sql = "select * from %s where id = '%s'";
		if (id == null) {
			return null;
		}
		final Map<String, FieldMeta> colMetaMap = this.getColMetaMap(clazz);

		sql = String.format(sql, tableName, id);
		T entity = null;
		try {
			entity = this.phoenixJdbcTemplete.queryForObject(sql, new HbaseRowMapper<T>(clazz, colMetaMap));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		entity.setId(id);
		return entity;
	}

	public <T extends IBaseEntity> List<T> query(Class<T> clazz, String sql) {
		final Map<String, FieldMeta> colMetaMap = this.getColMetaMap(clazz);
		return this.phoenixJdbcTemplete.query(sql, new HbaseRowMapper<T>(clazz, colMetaMap));
	}
	
	public List<Map<String,Object>> query(String sql) {
		return this.phoenixJdbcTemplete.queryForList(sql);
	}

	@Override
	public <T extends IBaseEntity> boolean exists(Class<T> clazz, String id) {
		String tableName = getHBaseTableName(clazz);
		String sql = "select 1 from %s where id='%s'";
		sql = String.format(sql, tableName, id);
		logger.debug(sql);
		Integer result = phoenixJdbcTemplete.queryForObject(sql, Integer.class);
		if (NumUtil.intValue(result) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 禁用！数据量太大全表查询会有风险！
	 */
	@Override
	public <T extends IBaseEntity> List<T> findAll(Class<T> clazz) {

		return null;
	}

	@Override
	public <T extends IBaseEntity> void create(T o) {
		create_new(o,null,null,null,null);

		// String tableName = this.getHBaseTableName(o.getClass());
		// if (o.getId() == null) {
		// BaseHBaseEntity t = (BaseHBaseEntity) o;
		// t.setId(t.generateRowKey());
		// }
		// String[] createSql = this.getCreateSql(o);
		// if (StringUtils.isEmpty(createSql[0]) ||
		// StringUtils.isEmpty(createSql[1])) {
		// return;
		// }
		//
		// String sql = "upsert into %s (%s) values (%s) ";
		// sql = String.format(sql, tableName, createSql[0], createSql[1]);
		// this.phoenixJdbcTemplete.execute(sql);

	}
	
	public  <T extends IBaseEntity> void create(T o, String trueTtl) {
		create_new(o,null,null,trueTtl,null);
	}

	private <T extends IBaseEntity> void create_new(T o, String generateRowKey, Integer number, String trueTtl, String timeStamp) {
		String tableName = this.getHBaseTableName(o.getClass());
		
		boolean bool = StringUtils.isEmpty(generateRowKey);//是否存在外键
		if(bool){
			if (o.getId() == null||o.getId() instanceof String &&StringUtils.isEmpty(o.getId().toString())) {
				BaseHBaseEntity t = (BaseHBaseEntity) o;
				generateRowKey = t.generateRowKey();
				t.setId(generateRowKey);
			} else {
				generateRowKey = o.getId().toString();
			}
		}else{
			//生成主键
			generateRowKey += "_" + number;
			o.setId(generateRowKey);
		}
		String[] createSql = this.getCreateSql(o);
		if (StringUtils.isEmpty(createSql[0]) || StringUtils.isEmpty(createSql[1])) {
			return;
		}
		
		String sql = "";
		if(!StringUtils.isEmpty(trueTtl)){
			BaseHBaseEntity g = (BaseHBaseEntity) o;
			timeStamp = g.setTimeStamp(trueTtl);
		}
		if(!StringUtils.isEmpty(timeStamp)){
			sql = "upsert into %s (%s,TSCREATEDDATE) values (%s,TO_DATE(%s, 'yyyy-MM-dd HH:mm:ss','GMT+8')) ";
			sql = String.format(sql, tableName, createSql[0], createSql[1], timeStamp);
		}
		else{
			sql = "upsert into %s (%s) values (%s) ";
			sql = String.format(sql, tableName, createSql[0], createSql[1]);
		}
		
		logger.debug(sql);
		if(o.getId() == null||o.getId() instanceof String &&StringUtils.isEmpty(o.getId().toString())){
			//如果rowkey为空或空字符串，拦截，不新增。
			throw new RuntimeException(o.getClass().getName()+"rowkey为空，程序错误代码，11110000");
		}
		System.out.println("======sql:"+sql);
		this.phoenixJdbcTemplete.execute(sql);	
		if(bool){
			// 插入关联表的数据
			if(!StringUtils.isEmpty(timeStamp)){
				this.createNToN(o, generateRowKey, timeStamp);
			}
			else{
				this.createNToN(o, generateRowKey, null);
			}
		}
	}

	/**
	 * 插入单个关联数据
	 * 
	 * @param o
	 * @param generateRowKey
	 * @param number
	 */
//	private <T extends IBaseEntity> void create(T o, String generateRowKey, int number) {
//		String tableName = this.getHBaseTableName(o.getClass());
//		if (tableName == null) {
//			return;
//		}
//		BaseHBaseEntity t = (BaseHBaseEntity) o;
//		generateRowKey += "_" + number;
//		t.setId(generateRowKey);
//
//		String[] createSql = this.getCreateSql(o);
//		if (StringUtils.isEmpty(createSql[0]) || StringUtils.isEmpty(createSql[1])) {
//			return;
//		}
//
//		String sql = "upsert into %s (%s) values (%s) ";
//		sql = String.format(sql, tableName, createSql[0], createSql[1]);
//		logger.debug(sql);
//		this.phoenixJdbcTemplete.execute(sql);
//	}

	/**
	 * 插入关联表的信息
	 * 
	 * @param obj
	 */
	private <T extends IBaseEntity> void createNToN(T obj, String generateRowKey, String timeStamp) {
		Class<T> clazz = (Class<T>) obj.getClass();
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		OneToMany otm = null;
		boolean isT = false;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			if (f.getDeclaringClass().isAssignableFrom(IBaseEntity.class)) {
				continue;
			}
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			if (otm == null) {
				continue;
			}
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}
			String qualifier = f.getName();
			// 如果是oneToMany,则需要生成多一张表
			if (otm != null) {
				try {
					pd = new PropertyDescriptor(f.getName(), clazz);
					// 获取到对象
					Object value = ClassUtils.getBeanPropertyValue(obj, qualifier);
					if (value == null) {
						continue;
					}
					// String fclassName = value.getClass().getName();

					String fclassName = pd.getPropertyType().getName();

					// 判断对象是否是集合容器
					if (!StringUtils.startsWith(fclassName, "java.lang")) {
						if (value instanceof java.util.Collection) {
							Collection<IBaseEntity> list = (Collection) value;
							int number = 0;
							for (IBaseEntity object : list) {
								create_new(object, generateRowKey, number, null, timeStamp);
								number++;
							}
						} else if (value instanceof java.util.Map) {
							Map<Object, IBaseEntity> map = (Map) value;
							int number = 0;
							for (Object key : map.keySet()) {
								create_new(map.get(key), generateRowKey, number,null, timeStamp);
								number++;
							}
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.debug("获取列属性出错");
				}

			}
		}
	}

	@Override
	public <T extends IBaseEntity> void create(List<T> instances) {
		if (CollectionUtils.isEmpty(instances)) {
			return;
		}
		for (T t : instances) {
			this.create(t);
		}
	}

	@Override
	public <T extends IBaseEntity> void update(T o) {
		this.create(o);

	}

	@Override
	public <T extends IBaseEntity> void update(List<T> instances) {
		this.create(instances);
	}

	@Override
	public <T extends IBaseEntity> void delete(Class<T> clazz, String id) {
		delete_new(clazz, id);
		// String tableName = this.getHBaseTableName(clazz);
		// String sql = "delete from %s where rowkey = '%s'";
		// sql = String.format(sql, tableName, id);
		// this.phoenixJdbcTemplete.execute(sql);
	}

	public <T extends IBaseEntity> void delete_new(Class<T> clazz, String id) {
		String tableName = this.getHBaseTableName(clazz);
		String sql = "delete from %s where id = '%s'";
		sql = String.format(sql, tableName, id);
		this.phoenixJdbcTemplete.execute(sql);
		this.findDeleteNToN(clazz, id);
	}

	/**
	 * 查找需要删除的信息
	 * 
	 * @param clazz
	 * @param id
	 */
	private <T extends IBaseEntity> void findDeleteNToN(Class<T> clazz, String id) {
		// TODO Auto-generated method stub
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		OneToMany otm = null;
		boolean isT = false;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			if (f.getDeclaringClass().isAssignableFrom(IBaseEntity.class)) {
				continue;
			}
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}
			String qualifier = f.getName();
			// 如果是oneToMany,则需要生成多一张表
			if (otm != null) {
				try {
					pd = new PropertyDescriptor(f.getName(), clazz);
					// 获取默认的构造器
					Constructor cons = clazz.getDeclaredConstructor(null);
					// 获取到对象
					Object value = ClassUtils.getBeanPropertyValue(cons.newInstance(null), qualifier);
					if (value == null) {
						continue;
					}
					// String fclassName = value.getClass().getName();

					String fclassName = pd.getPropertyType().getName();

					// 判断对象是否是集合容器
					if (!StringUtils.startsWith(fclassName, "java.lang")) {
						if (value instanceof java.util.Collection) {
							if (CollectionUtils.isEmpty((java.util.Collection<?>) value)) {
								isT = true;
							}
						} else if (value instanceof java.util.Map) {
							if (MapUtils.isEmpty((java.util.Map<?, ?>) value)) {
								isT = true;
							}
						}
					}
					if (isT) {
						Type fc = f.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
						if (fc == null)
							continue;
						if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
						{
							Class genericClazz = getFieldArgumentClass(f);
							// 删除
							this.deleteNToN(genericClazz, id);
						}
					}
					isT = false;
				} catch (Exception e) {

					logger.debug("获取列属性出错---" + e);
				}

			}

		}

	}

	/**
	 * 模糊删除相关的表信息
	 * 
	 * @param clazz
	 * @param id
	 */
	private <T extends IBaseEntity> void deleteNToN(Class<T> clazz, String id) {
		String tableName = this.getHBaseTableName(clazz);
		String sql = "delete from %s where id like '%s";
		sql = String.format(sql, tableName, id);
		sql += "%'";
		this.phoenixJdbcTemplete.execute(sql);
	}

	public <T extends IBaseEntity> void delete_new(T o) {
		if (o.getId() != null) {
			this.delete(o.getClass(), o.getId());
			return;
		}
		String sql = "delete from %s where %s";
		sql = String.format(sql, this.getHBaseTableName(o.getClass()), this.getWhereSql(o));
		this.phoenixJdbcTemplete.execute(sql);
	}

	@Override
	public <T extends IBaseEntity> void delete(T o) {
		if (o.getId() != null) {
			this.delete(o.getClass(), o.getId());
			return;
		}
		String sql = "delete from %s where %s";
		sql = String.format(sql, this.getHBaseTableName(o.getClass()), this.getWhereSql(o));
		this.phoenixJdbcTemplete.execute(sql);
	}

	@Override
	public <T extends IBaseEntity> void save(T o) {
		this.create(o);

	}

	@Override
	public <T extends IBaseEntity> void save(List<T> instances) {
		this.create(instances);
	}

	private <T extends IBaseEntity> void createTableCol(String tableName, String rowName) {
		String sql = "alter table  %s add if not exists %s varchar ";
		sql = String.format(sql, tableName, rowName);
		logger.debug(sql);
		System.out.println(sql);
		phoenixJdbcTemplete.execute(sql);

	}

//	public static void main(String[] args) {
//		System.out.println(new BasePhoenixBaseDao().getTableColSqlWithOneMany(SinglePlaneInfoEntity.class));
//	}

	@SuppressWarnings("unchecked")
	private <T extends IBaseEntity> String getTableColSqlWithOneMany(Class<T> clazz) {
		String tableName = this.getHBaseTableName(clazz);
		String sql = "";
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		HBaseColumn hca = null;
		OneToMany otm = null;
		Set<String> containFieldSet = new HashSet<String>();
		boolean isT = false;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			if (f.getDeclaringClass().isAssignableFrom(IBaseEntity.class)) {
				continue;
			}
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}
			String qualifier = f.getName();
			// 如果是oneToMany,则需要生成多一张表
			if (otm != null) {
				try {
					pd = new PropertyDescriptor(f.getName(), clazz);

					// Object value = ClassUtils.getBeanPropertyValue(obj,
					// qualifier);
					// 获取默认的构造器
					Constructor cons = clazz.getDeclaredConstructor(null);
					// 获取到对象
					Object value = ClassUtils.getBeanPropertyValue(cons.newInstance(null), qualifier);
					if (value == null) {
						continue;
					}
					// String fclassName = value.getClass().getName();

					String fclassName = pd.getPropertyType().getName();

					// 判断对象是否是集合容器
					if (!StringUtils.startsWith(fclassName, "java.lang")) {
						if (value instanceof java.util.Collection) {
							if (CollectionUtils.isEmpty((java.util.Collection<?>) value)) {
								isT = true;
							}
						} else if (value instanceof java.util.Map) {
							if (MapUtils.isEmpty((java.util.Map<?, ?>) value)) {
								isT = true;
							}
						}
					}
					if (isT) {
						Type fc = f.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
						if (fc == null)
							continue;
						if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
						{
							Class genericClazz = getFieldArgumentClass(f);
							// 递归建表
							// System.out.println(genericClazz + "--------" +
							// getTableColSql(genericClazz));
							createTable_new(genericClazz);
						}
					}
					isT = false;
				} catch (Exception e) {
					logger.debug("获取列属性出错---" + e);
				}

			}
			// System.out.println(qualifier);
			if ("id".equalsIgnoreCase(qualifier)) {
				continue;
			}
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}
			// if(!f.getName().equals(qualifier)){
			// System.out.println(qualifier+"==============="+f.getName());
			// }
			qualifier = "\""+qualifier+"\"";
			if (!containFieldSet.contains(qualifier)) {
				sql = sql + qualifier + " varchar,";
				containFieldSet.add(qualifier);
			} else {
				// System.out.println("表：" + tableName + " 属性重复：" + qualifier);
				logger.debug("表：" + tableName + " 属性：" + qualifier + "重复。。。。");
			}
		}
		return StringUtils.substringBeforeLast(sql, ",");
	}

	private <T extends IBaseEntity> String getTableColSql(Class<T> clazz) {
		String tableName = this.getHBaseTableName(clazz);
		String sql = "";
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		HBaseColumn hca = null;
		OneToMany otm = null;
		Set<String> containFieldSet = new HashSet<String>();
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			if (f.getDeclaringClass().isAssignableFrom(IBaseEntity.class)) {
				continue;
			}
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}

			if (otm != null) {
				// pd = new PropertyDescriptor(f.getName(), clazz);
			}
			String qualifier = f.getName();
			// System.out.println(qualifier);
			if ("id".equalsIgnoreCase(qualifier)) {
				continue;
			}
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}
			// if(!f.getName().equals(qualifier)){
			// System.out.println(qualifier+"==============="+f.getName());
			// }
			if (!containFieldSet.contains(qualifier)) {
				sql = sql + qualifier + " varchar,";
				containFieldSet.add(qualifier);
			} else {
				// System.out.println("表：" + tableName + " 属性重复：" + qualifier);
				logger.debug("表：" + tableName + " 属性：" + qualifier + "重复。。。。");
			}
		}
		return StringUtils.substringBeforeLast(sql, ",");
	}

	private <T extends IBaseEntity> String[] getCreateSql(T obj) {
		StringBuffer cols = new StringBuffer();
		StringBuffer values = new StringBuffer();
		String[] result = new String[2];
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(obj.getClass());
		HBaseColumn hca = null;
		OneToMany otm = null;
		boolean forceSave = false;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}

			if (otm != null) {
				// pd = new PropertyDescriptor(f.getName(), clazz);
			}
			String qualifier = f.getName();
			Object value = ClassUtils.getBeanPropertyValue(obj, qualifier);

			if (value == null) {
				continue;
			}
			String fclassName = value.getClass().getName();
			String format = null;
//			if ("id".equalsIgnoreCase(qualifier)) {
//				qualifier = "rowkey";
//			}
//			if("isTariffPackages".equalsIgnoreCase(qualifier)) {
//				continue;
//			}
//			if("tariffPackageDesc".equalsIgnoreCase(qualifier)) {
//				continue;
//			}
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				format = hca.format();
				forceSave = hca.forceSave();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}

			String valueStr = null;
			if (value instanceof Date && StringUtils.isNotEmpty(format)) {
				valueStr = DateUtil.dateToString((Date) value, format);
			} else if (!StringUtils.startsWith(fclassName, "java.lang")) {
				if (value instanceof java.util.Collection) {
					if (CollectionUtils.isEmpty((java.util.Collection<?>) value)&&!forceSave) {
						continue;
					}
				} else if (value instanceof java.util.Map) {
					if (MapUtils.isEmpty((java.util.Map<?, ?>) value)&&!forceSave) {
						continue;
					}
				}
				
				if(forceSave){
					valueStr = JSON.toJSONString(value);
				}
			} else {
				valueStr = value + "";
			}
			
			if(StringUtils.isNotEmpty(valueStr)){
				cols.append("\"").append(qualifier.toLowerCase()).append("\",");
				values.append("'").append(valueStr).append("',");
			}
			if("ID".equalsIgnoreCase(qualifier)&&StringUtils.isEmpty(valueStr)){
				throw new RuntimeException(obj.getClass().getName()+"在生成sql的过程中，rowkey值为空，错误代码：11110000");
			}
		}
		result[0] = StringUtils.substringBeforeLast(cols.toString(), ",");
		result[1] = StringUtils.substringBeforeLast(values.toString(), ",");
		return result;
	}

	private <T extends IBaseEntity> String getWhereSql(T obj) {
		String tableName = this.getHBaseTableName(obj.getClass());
		StringBuffer cols = new StringBuffer();
		StringBuffer values = new StringBuffer();
		StringBuffer where = new StringBuffer();
		String[] result = new String[2];
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(obj.getClass());
		HBaseColumn hca = null;
		OneToMany otm = null;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}

			if (otm != null) {
				// pd = new PropertyDescriptor(f.getName(), clazz);
			}
			String qualifier = f.getName();
			Object value = ClassUtils.getBeanPropertyValue(obj, qualifier);
			if (value == null) {
				continue;
			}
			String format = null;
			if ("id".equalsIgnoreCase(qualifier)) {
				continue;
			}
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				format = hca.format();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}
			String valueStr = null;
			if (value instanceof Date && StringUtils.isNoneEmpty(format)) {
				valueStr = DateUtil.dateToString((Date) value, format);
			} else {
				valueStr = value + "";
			}
			where.append("\"").append(qualifier.toLowerCase()).append("\"='").append(valueStr).append("',");
		}
		return StringUtils.substringBeforeLast(where.toString(), ",");
	}

	private <T extends IBaseEntity> void createTableCol(Class<T> clazz) {
		String tableName = this.getHBaseTableName(clazz);
		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		HBaseColumn hca = null;
		OneToMany otm = null;
		for (Field f : fields) {
			PropertyDescriptor pd = null;
			// 凡是集合对象的，暂不保存
			if (f.getAnnotation(ManyToOne.class) != null || f.getAnnotation(OneToOne.class) != null)
				continue;
			otm = f.getAnnotation(OneToMany.class);
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}

			if (otm != null) {
				// pd = new PropertyDescriptor(f.getName(), clazz);
			}
			String qualifier = f.getName();
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}
			this.createTableCol(tableName, qualifier);
		}
	}

	private Class getFieldArgumentClass(Field field) {
		ParameterizedType clz = (ParameterizedType) field.getGenericType();
		Class fieldType = (Class) clz.getActualTypeArguments()[0];
		return fieldType;
	}

	private <T> String getFamily(Class<T> entityClazz, HBaseColumn hca) {
		// 不再使用自定义列簇，多个列簇会影响性能
		// if(StringUtils.isNotEmpty(hca.family())){
		// return hca.family();
		// }
		HBaseEntity ta = entityClazz.getAnnotation(HBaseEntity.class);
		return ta.defaultFamily();
	}

	private <T extends IBaseEntity> Map<String, FieldMeta> getColMetaMap(Class<T> clazz) {
		Map<String, FieldMeta> colMetaMap = null;
		if (MapUtils.isNotEmpty(hbaseMata.get(clazz))) {
			colMetaMap = hbaseMata.get(clazz);
			return colMetaMap;
		}
		colMetaMap = new HashMap<String, FieldMeta>();

		List<Field> fields = null;
		fields = ClassUtils.getBeanAllFields(clazz);
		HBaseColumn hca = null;

		for (Field f : fields) {
			Transient tt = f.getAnnotation(Transient.class);
			if (tt != null) {
				continue;
			}
			String qualifier = f.getName();
			String format = null;
//			if ("id".equalsIgnoreCase(qualifier)) {
//				qualifier = "rowkey";
//			}
			hca = f.getAnnotation(HBaseColumn.class);
			if (hca != null) {
				// 使用columnDefinition与name作为列簇，列名
				// getFamily(clazz,hca);
				qualifier = hca.qualifier();
				format = hca.format();
				if (StringUtils.isEmpty(qualifier)) {
					qualifier = f.getName();
				}
			}
			Class type = f.getType();
			// if(type == java.util.Collection){
			// if(CollectionUtils.isEmpty((java.util.Collection<?>)value)){
			// continue;
			// }
			// }else if(value instanceof java.util.Map){
			// if(MapUtils.isEmpty((java.util.Map<?,?>)value)){
			// continue;
			// }
			// }
			FieldMeta fieldMeta = new FieldMeta();
			fieldMeta.setFieldName(f.getName());
			fieldMeta.setFormat(format);
			if (java.util.Collection.class.isAssignableFrom(type)) {
				Type genericFieldType = f.getGenericType();
				if (genericFieldType instanceof ParameterizedType) {
					ParameterizedType aType = (ParameterizedType) genericFieldType;
					Type[] fieldArgTypes = aType.getActualTypeArguments();
					if (fieldArgTypes != null) {
						for (Type fieldArgType : fieldArgTypes) {
							Class fieldArgClass = (Class) fieldArgType;
							if (fieldMeta.getArgTypes() == null) {
								List<Class<?>> list = new ArrayList<Class<?>>();
								fieldMeta.setArgTypes(list);
							}
							fieldMeta.getArgTypes().add(fieldArgClass);
						}
					}
				}
			}
			fieldMeta.setType(type);
			colMetaMap.put(qualifier, fieldMeta);
		}

		hbaseMata.put(clazz, colMetaMap);
		return colMetaMap;
	}

	protected <T extends IBaseEntity> String getHBaseTableName(Class<T> clazz) {
		HBaseEntity ta = clazz.getAnnotation(HBaseEntity.class);
		if (ta != null) {
			return ta.name();
		} else {
			return null;
		}
	}

	public JdbcTemplate getPhoenixJdbcTemplete() {
		return phoenixJdbcTemplete;
	}

	public void setPhoenixJdbcTemplete(JdbcTemplate phoenixJdbcTemplete) {
		this.phoenixJdbcTemplete = phoenixJdbcTemplete;
	}
}

class HbaseRowMapper<T> implements RowMapper<T> {
	Class<T> clazz = null;
	Map<String, FieldMeta> colMetaMap = null;

	public HbaseRowMapper(Class<T> clazz, Map<String, FieldMeta> colMetaMap) {
		this.clazz = clazz;
		this.colMetaMap = colMetaMap;
	}

	@Override
	public T mapRow(ResultSet arg0, int arg1) throws SQLException {
		T t = null;
		try {
			t = clazz.newInstance();
			Set<String> keys = colMetaMap.keySet();
			for (String key : keys) {
				FieldMeta colMeta = colMetaMap.get(key);
				String format = colMeta.getFormat();
				Class<?> type = colMeta.getType();
				String valueStr = arg0.getString(key);
				if (StringUtils.isEmpty(valueStr)) {
					continue;
				}
				Object value = null;
				if (Date.class.isAssignableFrom(type) && StringUtils.isNotEmpty(format)) {
					value = DateUtil.stringToDate(valueStr, format);
				} else if (type != null && (StringUtils.startsWith(type.getName(), "java.lang")
						|| type == java.io.Serializable.class)) {
					value = valueStr;
				} else if (type != null && java.util.Collection.class.isAssignableFrom(type)) {
					if (CollectionUtils.isNotEmpty(colMeta.getArgTypes())) {
						List<?> valuetemp = JSON.parseArray(valueStr, colMeta.getArgTypes().get(0));
						if (valuetemp == null) {
							continue;
						}
						if (type.isAssignableFrom(valuetemp.getClass())) {
							value = valuetemp;
						} else if (java.util.Set.class.isAssignableFrom(type)) {
							Set<Object> set = new HashSet<Object>();
							for (Object obj : valuetemp) {
								set.add(obj);
							}
							value = set;
						}

					} else {
						value = JSON.parseObject(valueStr, type);
					}
				} else {
					value = JSON.parseObject(valueStr, type);
				}
				ClassUtils.setProperty(colMeta.getFieldName(), value, t);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return t;
	}

}
