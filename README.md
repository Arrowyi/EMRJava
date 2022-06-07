# Pattern : Expectation miss record

this is the java lib for Expectation miss record

## The problem
Log analysis is one of the main daily tasks for the programmers. some scenario is that we find the point the error occurred by analyzing the log files, but we are not sure why the error occurred, what cause the error there. sometimes the crime scene is far away from the root cause, and we can't find out the root cause just from the log around the point the error occurred. so we have to walk up the log files carefully, sometimes even though we spend a lot of time to walk up the log files, we still can't find the root cause, just because there are not enough loges to confirm the root cause or just clear the suspicion of some condition.

## Example
look at the below code:

```java
class DataInfo
{
   // ..... 
}
 
DataInfo getDataInfoFromOther(Others o)
{
    if (o != null && someConditionIsTrue(o))
    {
        return new DataInfo(o);
    }
 
    return null;
}
 
DataInfo2 getDataInfo2(DataInfo dataInfo)
{
    if(dataInfo != null && someConditionIsTrue(dataInfo))
    {
        return new DataInfo2(dataInfo);
    }
 
    return null;
}
 
boolean handleOthers2(Others2 o2)
{
    if(o2 != null)
    {
        //handle with o2
 
        return true;
    }
 
    return false;
}
 
void doAction(Others o, Others2 o2)
{
    DataInfo2 dataInfo2= getDataInfo2(getDataInfoFromOther(o));
 
    if(dataInfo2 != null && handleOthers2(o2))
    {
        showDialogWith(dataInfo2);
    }
    
}
```
If the dialog wasn't showed in the user flow, what was the root cause , could you find out it from the code logic ? it could be the o is null or o is not satisfied the condition check, or the dataInfo is not satisfied the condition check or just the Others2 is null , etc.
if it is the error occurred in the online product, there is no way to locate the root cause from the log files, you have to reproduce it yourself or add log than ask the customer to reproduce it and catch the loges again.

## Solution1
there are already some pattern or principle for that annoyance, one of them is the **Defensive programming** or **Fail fast**  , we can refine the above code like this :
```java
class DataInfo
{
   // ....
}
 
DataInfo getDataInfoFromOther(Others o)
{
    checkNotNull(o);
 
    if (someConditionIsTrue(o))
    {
        return new DataInfo(o);
    }
    else
    {
        Log("some condition is not true of o");
    }
 
    return null;
}
 
DataInfo2 getDataInfo2(DataInfo dataInfo)
{
    checkNotNull(dataInfo);
 
    if(someConditionIsTrue(dataInfo))
    {
        return new DataInfo2(dataInfo);
    }
    else
    {
        Log("some condition is not true of dataInfo");
    }
 
    return null;
}
 
boolean handleOhters2(Others2 o2)
{
    checkNotNull(o2);
     
    // handle something
 
    return true;
}
 
void doAction(Others o, Others2 o2)
{
    DataInfo2 dataInfo2= getDataInfo2(getDataInfoFromOther(o));
    checkNotNull(dataInfo2);
    if(handleOthers2(o2))
    {
        showDialogWith(dataInfo2);
    }
}
```
The **checkNotNull** function is from the Condition class of google Guava lib. It throws the NPE if the parameter is null.
now if the dialog didn't show, we could find the root cause by the log files immediately , because there is no NPE , the parameters mustn't be null, and then ,we can check the log files, which condition is not true.

but , the NPE is too roughly to the real products. sometimes, we only need record the exception but not the crash. we can write code like this :
## Solution2
```java
class DataInfo
{
    // ....
}
 
DataInfo getDataInfoFromOther(Others o)
{
    if(o == null)
    {
        Log("o is null in getDataInfoFromOther");
        return null;
    }
 
    if (someConditionIsTrue(o))
    {
        return new DataInfo(o);
    }
    else
    {
        Log("some condition is not true of o");
    }
 
    return null;
}
 
DataInfo2 getDataInfo2(DataInfo dataInfo)
{
    if(dataInfo == null)
    {
        Log("dataInfo is null in getDataInfo2");
        return null
    }
 
    if(someConditionIsTrue(dataInfo))
    {
        return new DataInfo2(dataInfo);
    }
    else
    {
        Log("some condition is not true of dataInfo");
    }
 
    return null;
}
 
boolean handleOhters2(Others2 o2)
{
    if(o2 == null)
    {
        Log("o2 is null in handleOthers2");
        return false;
    }
  
    // handle something
 
    return true;
}
 
void doAction(Others o, Others2 o2)
{
    DataInfo2 dataInfo2= getDataInfo2(getDataInfoFromOther(o));
    if(dataInfo2 == null)
    {
        Log("dataInfo2 is null in doAction");
        return;
    }
 
    if(handleOthers2(o2))
    {
        showDialogWith(dataInfo2);
    }
}
```
It works, but tedious. all the programmers are lazy, we can make our life more easier.
we can create the EMR lib for ourselves :

## The Expectation miss record 
```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static indi.arrowyi.emr.Emr.*;
import static org.junit.jupiter.api.Assertions.*;

class DataInfo
{
    // ....
}

DataInfo getDataInfoFromOther(Others o)
{
    if (notNULL(o) && beTrue(someConditionCheck(o)))
    {
        return new DataInfo(o);
    }
    
    return null;
}
    
DataInfo2 getDataInfo2(DataInfo dataInfo)
{
    if(notNull(dataInfo) 
            && beTrue(someConditionIsTrue(dataInfo)))
    {
        return new DataInfo2(dataInfo);
    }
    
    return null;
}

boolean handleOhters2(Others2 o2)
{
    if(notNull(o2)){
        // handle something
        return true;
    }
    
    return false;
}

void doAction(Others o, Others2 o2)
{
    if(notNull(getDataInfo2(getDataInfoFromOther(o)))
    && handleOthers2(o2)) 
    {
        showDialogWith(dataInfo2);
    }
}

```

See, the world is fluency and the code is more readable than before. 

If there is some status not satisfy the condition, we will never lose the info , and from the log, we will get the info like this :
```java
should not be null
Expectation miss record : at EmrTest.java
indi.arrowyi.emr.EmrTest  
testBeTrue  53
```
with what , we will locate the expectation miss immediately.

## How to use
### reference the lib
Gradle

Add it in your root build.gradle at the end of repositories:
```kotlin
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```kotlin
dependencies {
	        implementation 'com.github.Arrowyi:EMRJava:main-SNAPSHOT'
	}
```

Maven

Add the JitPack repository to your build file
```kotlin
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Add the dependency
```kotlin
<dependency>
	    <groupId>com.github.Arrowyi</groupId>
	    <artifactId>EMRJava</artifactId>
	    <version>main-SNAPSHOT</version>
	</dependency>
```
### The API

```java

/**
     * Init log interface.
     *
     * @param logInterface the log interface
     */
    public static void initLogInterface(LogInterface logInterface)


/**
 * check if the parameter is null.
 *
 * @param o the object you want to check
 * @return the boolean , if o is not null return true , else false with outputting the log
 */
public static boolean notNull(Object o)

/**
 * check the parameter is true
 *
 * @param check value to be check
 * @return the boolean, true if the value is true, or false with outputting the log
 */
public static boolean beTrue(boolean check)

/**
 * check the parameter is false
 *
 * @param check value to be check
 * @return the boolean, true if the value is false, or false with outputting the log
 */
public static boolean beFalse(boolean check)

/**
 * check the value is null
 *
 * @param check the value to be check
 * @return the boolean, true if the value is null, or false with outputting the log
 */
public static boolean beNull(Object check)
```

More usage you could ref to the test class , it is so simple. 
I wish it could make your lift easier.
