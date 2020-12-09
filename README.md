# Replica System For Student Records
Following are the commands and the instructions to run ANT on your project.


Note: build.xml is present in studentskills/src folder.

## Starting directory:
```commandline
dfan2@remote07:~/replica_system_for_student_records$ pwd
/home/dfan2/replica_system_for_student_records
```

## Instruction to clean:

```commandline
ant -buildfile studentskills/src/build.xml clean
```

Description: It cleans up all the .class files that were generated when you
compiled your code.

## Instructions to compile:

```commandline
ant -buildfile studentskills/src/build.xml all
```
The above command compiles your code and generates .class files inside the BUILD folder.

## Instructions to run:

```commandline
ant -buildfile studentskills/src/build.xml run -Dinput="input.txt" -Dmodify="modify.txt" -Dout1="out1.txt" -Dout2="out2.txt" -Dout3="out3.txt" -Derror="error.txt" -Ddebug="READ_LINE, INSERT_NODE, MODIFY_NODE"
```
Note: Arguments accept the absolute path of the files.

Legal args for -Ddebug:  
READ_LINE, PARSE_LINE, INSERT_NODE, MODIFY_NODE

## Instructions to clean, compile then run:

```commandline
ant -buildfile studentskills/src/build.xml clean all run -Dinput="input.txt" -Dmodify="modify.txt" -Dout1="out1.txt" -Dout2="out2.txt" -Dout3="out3.txt" -Derror="error.txt" -Ddebug="READ_LINE, INSERT_NODE, MODIFY_NODE"
```


## Description:  

Briefly describe of the implementation of observer pattern:  
- When a node inserted into a tree, several node replicas will be inserted into each corresponding tree replica and mutually register as both subjects and observers.
- When a node need to be updated. treeHelper invokes studentRecord.updateStudentRecord() method to update.  
- updateStudentRecord() invokes update() to update itself and then invokes notifyObservers() to notify all the observers.

AVL Tree refers to:  
https://www.geeksforgeeks.org/avl-tree-set-1-insertion/
https://algorithms.tutorialhorizon.com/avl-tree-insertion/

Time complexity:  
h is height of the tree. n is the total number of nodes.  
Time complexity of searching and inserting for a BST is O(h) in worst-case. The problem is h may equal to n in worst-case for a BST.  
AVL tree is a self-balancing Binary Search Tree. Self-balancing means differences between heights of left and right subtrees for every node is less than or equal to 1.  
So Height of the AVL tree remains O(Logn) and time complexity of searching and inserting for a BST is O(Logn) even in worst-case.  

MyLogger imports from:
http://www.cs.binghamton.edu/~mgovinda/courses/downloads/MyLogger.java  
and modified  

Test on dfan2@remote.cs.binghamton.edu  
- java version "1.8.0_201"
- Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
- Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
