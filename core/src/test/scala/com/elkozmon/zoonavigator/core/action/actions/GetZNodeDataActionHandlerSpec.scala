/*
 * Copyright (C) 2018  Ľuboš Kozmon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elkozmon.zoonavigator.core.action.actions

import com.elkozmon.zoonavigator.core.curator.CuratorSpec
import com.elkozmon.zoonavigator.core.utils.CommonUtils._
import com.elkozmon.zoonavigator.core.zookeeper.znode.ZNodePath
import monix.execution.Scheduler
import org.apache.curator.framework.CuratorFramework
import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration._

@SuppressWarnings(Array("org.wartremover.warts.Null"))
class GetZNodeDataActionHandlerSpec extends FlatSpec with CuratorSpec {

  import Scheduler.Implicits.global

  private def actionHandler(implicit curatorFramework: CuratorFramework) =
    new GetZNodeDataActionHandler(curatorFramework)

  "GetZNodeDataActionHandler" should "return empty byte array for node with null data" in withCurator {
    implicit curatorFramework =>
      curatorFramework
        .create()
        .forPath("/null-node", null)
        .discard()

      val action = GetZNodeDataAction(ZNodePath.unsafe("/null-node"))

      val metadata =
        Await.result(actionHandler.handle(action).runAsync, Duration.Inf)

      assert(metadata.data.bytes.isEmpty)
  }
}
