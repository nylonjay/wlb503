package com.bigkoo.convenientbanner.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.bankscene.bes.welllinkbank.R;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.ConvenientBanner.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BannerUtils
{

	public static void startTurning(ConvenientBanner convenientBanner)
	{
		//开始自动翻页
		if(convenientBanner != null &&
				convenientBanner.getVisibility() == View.VISIBLE)
		{
			convenientBanner.startTurning(3000);
		}
	}

	public static void stopTurning(ConvenientBanner convenientBanner)
	{
		//开始自动翻页
		if(convenientBanner != null)
		{
			convenientBanner.stopTurning();
		}
	}

	public static ConvenientBanner initImageBar(Context context, ConvenientBanner convenientBanner,
												List<String> networkImages , final List<Map<String, String>> imageInfoMapList)
	{
//		convenientBanner = (ConvenientBanner) ((Activity)context).findViewById(R.id.convenientBanner);
		convenientBanner.setVisibility(View.VISIBLE);

		if(networkImages == null ||
				networkImages.isEmpty())
		{
			ArrayList<Integer> localImages = new ArrayList<Integer>();
			localImages.add(R.mipmap.banner);
//		localImages.add(R.drawable.banner02);
//		localImages.add(R.drawable.banner03);
//		localImages.add(R.drawable.banner04);
			//本地图片例子
			convenientBanner.setPages(
					new CBViewHolderCreator<LocalImageHolderView>() {
						@Override
						public LocalImageHolderView createHolder() {
							return new LocalImageHolderView();
						}
					}, localImages)
					//设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
					.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
					//设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
					//设置翻页的效果，不需要翻页效果可用不设
					.setPageTransformer(Transformer.DefaultTransformer);
		}
		else
		{
			//网络加载例子
//        networkImages=Arrays.asList(images);
			convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
				@Override
				public NetworkImageHolderView createHolder() {
					return new NetworkImageHolderView(imageInfoMapList);
				}
			},networkImages) //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
					.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
					//设置指示器的方向
//        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
					//设置翻页的效果，不需要翻页效果可用不设
					.setPageTransformer(Transformer.DefaultTransformer);
		}

		return convenientBanner;
	}

	//add by wrt
	//初始化网络图片缓存库
//	public static void initImageLoader(Context context){
//        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
//                showImageForEmptyUri(R.drawable.logo)
////                .cacheInMemory(true).cacheOnDisk(true)
//                .cacheInMemory(true).cacheOnDisk(false)
//                .extraForDownloader(CSIICommunicationActionNew.getHeaderParamsMap())
//                .build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//        		context).defaultDisplayImageOptions(defaultOptions)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .imageDownloader(new CustomImageDownaloder(context))
//                .build();
//        ImageLoader.getInstance().init(config);
//    }
	//add end
}
