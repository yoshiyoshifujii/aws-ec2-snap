## Assembly

```sh
$ sbt assembly
```

## Deploy

```sh
$ cd ./serverless
$ VOLUME_ID=<VOLUME_ID> DESCRIPTION=<DESCRIPTION> serverless deploy -r <REGION> --role <ROLE_ARN>
```

