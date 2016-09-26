package org.hisrc.ptbatch.pte.service;

import de.schildbach.pte.BahnProvider;

public class BahnProviderService extends AbstractNetworkProviderService{

    public BahnProviderService() {
        super(new BahnProvider());
    }
}
