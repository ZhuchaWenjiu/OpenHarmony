package com.example.bailian_shop.move;

import com.example.bailian_shop.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.global.resource.RawFileDescriptor;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.common.Source;
import ohos.media.player.Player;

public class ShipingAbility extends AbilitySlice {

    static final HiLogLabel logLabel=new HiLogLabel(HiLog.LOG_APP,0x001010,"视频测试");

    private static Player player;

    private SurfaceProvider sfProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_shiping);

        player=new Player(getContext());

        sfProvider=new SurfaceProvider(getContext());

        sfProvider.getSurfaceOps().get().addCallback(new VideoSurfaceCallback());

        sfProvider.pinToZTop(true);

        DependentLayout dependentLayout=(DependentLayout)findComponentById(ResourceTable.Id_shiping);

        if(dependentLayout!=null) {

            dependentLayout.addComponent(sfProvider);
        }
    }
    private void playLocalFile(Surface surface) {

        try {

            RawFileDescriptor filDescriptor = getResourceManager().getRawFileEntry("resources/rawfile/shipin_bailian.mp4").openRawFileDescriptor();

            Source source = new Source(filDescriptor.getFileDescriptor(),filDescriptor.getStartPosition(),filDescriptor.getFileSize());
            //设置循环播放
            /* player.enableSingleLooping(true);*/

            player.setSource(source);

            player.setVideoSurface(surface);

            player.setPlayerCallback(new VideoPlayerCallback());

            player.prepare();

            sfProvider.setTop(0);

            player.play();


        } catch (Exception e) {

            HiLog.info(logLabel,"playUrl Exception:" + e.getMessage());
        }

    }

    @Override
    public void onActive() {

        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {

        super.onForeground(intent);

    }

    private class VideoSurfaceCallback implements SurfaceOps.Callback {

        @Override
        public void surfaceCreated(SurfaceOps surfaceOps) {
            //当画面可见的时候执行
            /* player.play();*/

            HiLog.info(logLabel,"surfaceCreated() called.");

            if (sfProvider.getSurfaceOps().isPresent()) {

                Surface surface = sfProvider.getSurfaceOps().get().getSurface();

                playLocalFile(surface);
            }

        }

        @Override
        public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {
            //当画面发生变化执行
            HiLog.info(logLabel,"surfaceChanged() called.");
        }

        @Override
        public void surfaceDestroyed(SurfaceOps surfaceOps) {
            //当画面不见得时候执行
            /* player.stop();*/
            HiLog.info(logLabel,"surfaceDestroyed() called.");
        }

    }

    private class VideoPlayerCallback implements Player.IPlayerCallback {

        @Override
        public void onPrepared() {

            HiLog.info(logLabel,"onPrepared");
        }

        @Override
        public void onMessage(int i, int i1) {

            HiLog.info(logLabel,"onMessage");
        }

        @Override
        public void onError(int i, int i1) {

            HiLog.info(logLabel,"onError: i=" + i + ", i1=" + i1);
        }

        @Override
        public void onResolutionChanged(int i, int i1) {

            HiLog.info(logLabel,"onResolutionChanged");
        }

        @Override
        public void onPlayBackComplete() {

            HiLog.info(logLabel,"onPlayBackComplete");

            if (player != null) {

                player.stop();

                player = null;

            }
        }

        @Override
        public void onRewindToComplete() {

            HiLog.info(logLabel,"onRewindToComplete");
        }

        @Override
        public void onBufferingChange(int i) {

            HiLog.info(logLabel,"onBufferingChange");
        }

        @Override
        public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {

            HiLog.info(logLabel,"onNewTimedMetaData");
        }

        @Override
        public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {

            HiLog.info(logLabel,"onMediaTimeIncontinuity");

        }
    }
}
