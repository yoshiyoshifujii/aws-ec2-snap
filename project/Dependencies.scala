import sbt._

object Dependencies {
  // Amazon
  val awsSDKVersion = "1.11.167"
  val awsSDKEC2     = "com.amazonaws" % "aws-java-sdk-ec2" % awsSDKVersion
  val awsLambdaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"

  val rootDeps = Seq(
    awsLambdaCore,
    awsSDKEC2
  )

}
