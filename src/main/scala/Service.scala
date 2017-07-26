import java.time.{Instant, ZoneId, ZonedDateTime}

import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model._
import com.amazonaws.services.lambda.runtime.LambdaLogger

import scala.collection.JavaConverters._
import scala.util.Try

class Service(val logger: LambdaLogger) {

  private val ec2Client = AmazonEC2Client.builder().build()

  def snap(volumeId: String, description: Option[String]): Unit = {
    import Traverse._

    val now      = ZonedDateTime.now(ZoneId.of("UTC"))
    val aWeekAgo = Instant.from(now.minusWeeks(1L)).getEpochSecond * 1000
    (for {
      _                       <- createSnapshot(volumeId, description)
      describeSnapshotsResult <- describeSnapshots(volumeId)
      _ <- describeSnapshotsResult.getSnapshots.asScala
        .filter(_.getVolumeId == volumeId)
        .filter(_.getStartTime.getTime < aWeekAgo)
        .map(_.getSnapshotId)
        .map(deleteSnapshot)
        .toTrySeq
    } yield ()).get

  }

  private def createSnapshot(volumeId: String,
                             description: Option[String]): Try[CreateSnapshotResult] = Try {
    val createSnapshotRequest = new CreateSnapshotRequest()
      .withVolumeId(volumeId)
    description.foreach(d => createSnapshotRequest.setDescription(d))
    val createSnapshotResult = ec2Client.createSnapshot(createSnapshotRequest)
    logger.log(s"Create Snapshot: ${createSnapshotResult.getSnapshot.getSnapshotId}")
    createSnapshotResult
  }

  private def describeSnapshots(volumeId: String): Try[DescribeSnapshotsResult] = Try {
    val describeSnapshotsRequest = new DescribeSnapshotsRequest()
    ec2Client.describeSnapshots(describeSnapshotsRequest)
  }

  private def deleteSnapshot(snapshotId: String): Try[DeleteSnapshotResult] = Try {
    val deleteSnapshotRequest = new DeleteSnapshotRequest(snapshotId)
    val deleteSnapshotResult  = ec2Client.deleteSnapshot(deleteSnapshotRequest)
    logger.log(s"Delete Snapshot: $snapshotId")
    deleteSnapshotResult
  }

}
