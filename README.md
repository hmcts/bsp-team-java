Run ZipTest class to get memory usage and duration of signature verification of a small and a big zip files.

The size of the small zip file resources/small/envelope.zip is 3 KB, The size of the small zip file resources/big/envelope.zip is 105 MB.

Each file is verified 2 times: 


- using existing approach (as in blog-router-service)
- using optimized memory consumption

These are examples of console output:
```
Verifying folder 'small' with memory usage optimization
Heap used before verification: 4194304
Data size: 2710
Heap used after verification: 4194304
Verified folder 'small' with memory usage optimization in 32 ms
```

```
Verifying folder 'small' without memory usage optimization
Heap used before verification: 5242880
Data size: 2710
Heap used after verification: 5242880
Verified folder 'small' without memory usage optimization in 3 ms
```

```
Verifying folder 'big' with memory usage optimization
Heap used before verification: 5242880
Data size: 104748914
Heap used after verification: 6291456
Verified folder 'big' with memory usage optimization in 205616 ms
```

```
Verifying folder 'big' without memory usage optimization
Heap used before verification: 6291456
Data size: 104748914
Heap used after verification: 110100480
Verified folder 'big' without memory usage optimization in 731 ms
```

Optimization of memory usage makes big difference for the big file:
1. Memory usage ~6 MB vs ~110 MB
2. Duration ~205 sec vs <1 sec
