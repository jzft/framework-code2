Run AS——>Maven Build… 
mybatis-generator:generate 

每次生成要把对应的mapper。xml删除
生成后记得要删除mapper的所有方法,且修改继承BaseMapper泛型<Entity,Example>
