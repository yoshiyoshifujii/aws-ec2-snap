import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

trait BaseHandler extends RequestStreamHandler {

  override def handleRequest(input: InputStream, output: OutputStream, context: Context) = {
    val volumeId    = sys.env.get("VOLUME_ID")
    val description = sys.env.get("DESCRIPTION")
    val logger      = context.getLogger
    val service     = new Service(logger)

    volumeId.fold {
      logger.log("set the environment variable VOLUME_ID.")
    } { vid =>
      service.snap(vid, description)
    }
  }

}

class Handler extends BaseHandler
