package com.utsman.listing.di

import com.utsman.abstraction.di.Module
import com.utsman.listing.domain.InstalledAppUseCase
import com.utsman.listing.domain.PagingUseCase
import com.utsman.listing.viewmodel.InstalledAppsViewModel
import com.utsman.listing.viewmodel.PagingViewModel

lateinit var pagingUseCase: Module<PagingUseCase>
lateinit var installedAppUseCase: Module<InstalledAppUseCase>

lateinit var pagingViewModel: Module<PagingViewModel>
lateinit var installedAppViewModel: Module<InstalledAppsViewModel>